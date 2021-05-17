package com.atguitu.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.R;
import com.atguitu.gulimall.cart.feign.ProductFeignService;
import com.atguitu.gulimall.cart.interceptor.CartInterceptor;
import com.atguitu.gulimall.cart.service.CartService;
import com.atguitu.gulimall.cart.vo.Cart;
import com.atguitu.gulimall.cart.vo.CartItem;
import com.atguitu.gulimall.cart.vo.SkuInfoVo;
import com.atguitu.gulimall.cart.vo.UserInfoTo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private ThreadPoolExecutor executor;

    private final String CART_PREFIX = "MAPLE:cart:";


    /**
     * 将商品添加到购物车
     */
    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException{
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String res = (String) cartOps.get(skuId.toString());
        //1.购物车无此商品，要添加新商品到购物车
        if(StringUtils.isEmpty(res)){
            //新建购物车商品
            CartItem cartItem = new CartItem();
            // 异步编排
            CompletableFuture<Void> getSkuInfo = CompletableFuture.runAsync(() -> {
                // 1. 远程查询当前要添加的商品的信息
                R skuInfo = productFeignService.SkuInfo(skuId);
                SkuInfoVo sku = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {});
                // 2. 添加新商品到购物车
                cartItem.setCount(num);
                cartItem.setCheck(true);
                cartItem.setImage(sku.getSkuDefaultImg());
                cartItem.setPrice(sku.getPrice());
                cartItem.setTitle(sku.getSkuTitle());
                cartItem.setSkuId(skuId);
            }, executor);

            // 3. 远程查询sku组合信息
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> values = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(values);
            }, executor);

            CompletableFuture.allOf(getSkuInfo, getSkuSaleAttrValues).get();
            // CartItem 序列化为 JSON 存入 Redis
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
        //2.购物车有此商品，数量相加
        else{
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount() + num);
            cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    /**
     * 获取到我们要操作的购物车 [已经包含用户前缀 只需要带上用户id 或者临时id 就能对购物车进行操作]
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        // 1. 这里我们需要知道操作的是离线购物车还是在线购物车
        String cartKey = CART_PREFIX;
        if(userInfoTo.getUserId() != null){
            log.debug("\n用户 [" + userInfoTo.getUsername() + "] 正在操作购物车");
            // 已登录的用户购物车的标识
            cartKey += userInfoTo.getUserId();
        }else{
            log.debug("\n临时用户 [" + userInfoTo.getUserKey() + "] 正在操作购物车");
            // 未登录的用户购物车的标识
            cartKey += userInfoTo.getUserKey();
        }
        // 绑定这个 key 以后所有对redis 的操作都是针对这个key
        return stringRedisTemplate.boundHashOps(cartKey);
    }

    /**
     * 获取购物车里的某个商品
     */
    @Override
    public CartItem getCartItem(Long skuId) {
        //获取当前购物车
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //Redis中获取 购物商品信息（String）
        String o = (String) cartOps.get(skuId.toString());
        //String -> CartItem
        return JSON.parseObject(o, CartItem.class);
    }

    /**
     * 获取购物车信息
     * 浏览器有一个cookie：user-key 标识用户身份 一个月后过期
     * 每次访问都会带上这个 user-key
     * 如果没有临时用户 还要帮忙创建一个
     */
    @Override
    public Cart getCart() throws ExecutionException, InterruptedException{
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        Cart cart = new Cart();
        // 临时购物车的key
        String tempCartKey = CART_PREFIX + userInfoTo.getUserKey();
        if(userInfoTo.getUserId() != null){
            // 1. 已登录 对用户的购物车进行操作
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            // 1.1 如果临时购物车的数据没有进行合并
            List<CartItem> tempItem = getCartItems(tempCartKey);
            if(tempItem != null){
                // 1.2 临时购物车有数据 则进行合并
                log.info("\n[" + userInfoTo.getUsername() + "] 的购物车已合并");
                for (CartItem cartItem : tempItem) {
                    addToCart(cartItem.getSkuId(), cartItem.getCount());
                }
                // 1.3 清空临时购物车
                clearCart(tempCartKey);
            }
            // 1.4 获取登录后的购物车数据 [包含合并过来的临时购物车数据]
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }else {
            // 2. 没登录 获取临时购物车的所有购物项
            cart.setItems(getCartItems(tempCartKey));
        }
        return cart;
    }

    /**
     * 获取购物车所有项
     */
    private List<CartItem> getCartItems(String cartKey){
        BoundHashOperations<String, Object, Object> hashOps = stringRedisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        if(values != null && values.size() > 0){
            return values.stream().map(obj -> JSON.parseObject((String) obj, CartItem.class)).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 清除购物车
     * @param cartKey
     */
    @Override
    public void clearCart(String cartKey){
        stringRedisTemplate.delete(cartKey);
    }

    /**
     * 是否勾选商品
     */
    @Override
    public void checkItem(Long skuId, Integer check) {
        // 获取要选中的购物项
        CartItem cartItem = getCartItem(skuId);
        //如果check = 1 则选中
        cartItem.setCheck(check == 1);
        //获取当前购物车，并绑定了Redis信息，保证当前操作 只改变当前购物车数据
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //向 Redis 中当前购物车更新数据
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }


    /**
     * 改变商品数量（点击加减）
     */
    @Override
    public void changeItemCount(Long skuId, Integer num) {
        //获取原购物商品信息（防止覆盖）
        CartItem cartItem = getCartItem(skuId);
        //更新其数量为新传来的值 num
        cartItem.setCount(num);
        //获取当前购物车，且绑定好了其Redis中的数据
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //向Redis中更新当前商品数据
        cartOps.put(skuId.toString(), JSON.toJSONString(cartItem));
    }


    /**
     * 删除商品
     */
    @Override
    public void deleteItem(Long skuId) {
        //获取当前购物车，且绑定了 Redis 操作
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        //在 Redis 中直接删除记录
        cartOps.delete(skuId.toString());
    }

    /**
     * 当前用户购物车数据
     */
    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if(userInfoTo.getUserId() == null){
            return null;
        }
        //用户登录
        else{
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            // 获取所有被选中的购物项
            List<CartItem> collect = cartItems.stream().filter(item -> item.getCheck()).map(item -> {
                try {
                    //更新最新价格
                    R r = productFeignService.getPrice(item.getSkuId());
                    String price = (String) r.get("data");
                    item.setPrice(new BigDecimal(price));
                } catch (Exception e) {
                    log.warn("远程查询商品价格出错 [商品服务未启动]");
                }
                return item;
            }).collect(Collectors.toList());
            return collect;
        }
    }

}









