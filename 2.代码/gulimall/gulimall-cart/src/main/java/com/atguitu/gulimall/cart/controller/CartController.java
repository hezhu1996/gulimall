package com.atguitu.gulimall.cart.controller;

import com.atguitu.gulimall.cart.service.CartService;
import com.atguitu.gulimall.cart.vo.Cart;
import com.atguitu.gulimall.cart.vo.CartItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Controller
public class CartController {

    private final String PATH = "redirect:http://cart.gulimall.com/cart.html";

    @Autowired
    private CartService cartService;


    /**
     * 当前用户购物车数据
     */
    @ResponseBody //返回 JSON数据
    @GetMapping("/currentUserCartItems")
    public List<CartItem> getCurrentUserCartItems(){

        return cartService.getUserCartItems();
    }


    /**
     * 获取购物车信息
     */
    @GetMapping({"/","/cart.html"})
    public String carListPage(Model model) throws ExecutionException, InterruptedException {
        //重新获取购物车数据
        Cart cart = cartService.getCart();
        model.addAttribute("cart", cart);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * 	RedirectAttributes.addAttribute: 会自动将数据添加到url后面
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            RedirectAttributes redirectAttributes) throws ExecutionException, InterruptedException {

        cartService.addToCart(skuId, num);
        redirectAttributes.addAttribute("skuId", skuId);
        // 重定向到成功页面
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    /**
     * 解决重复提交问题，跳转到成功页
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam(value = "skuId",required = false) Object skuId, Model model){
        CartItem cartItem = null;
        // 然后在查一遍 购物车
        if(skuId == null){
            model.addAttribute("item", null);
        }else{
            try {
                //获取购物车里的某个商品
                cartItem = cartService.getCartItem(Long.parseLong((String)skuId));
            } catch (NumberFormatException e) {
                log.warn("恶意操作! 页面传来非法字符.");
            }
            model.addAttribute("item", cartItem);
        }
        return "success";
    }

    /**
     * 是否勾选商品
     */
    @GetMapping("checkItem.html")
    public String checkItem(@RequestParam("skuId") Long skuId, @RequestParam("check") Integer check){
        cartService.checkItem(skuId, check);
        return PATH;
    }

    /**
     * 改变商品数量（点击加减）
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId, @RequestParam("num") Integer num){
        cartService.changeItemCount(skuId, num);
        return PATH;
    }

    /**
     * 删除商品
     */
    @GetMapping("/deleteItem")
    public String deleteItem(@RequestParam("skuId") Long skuId){
        cartService.deleteItem(skuId);
        return PATH;
    }
}

















