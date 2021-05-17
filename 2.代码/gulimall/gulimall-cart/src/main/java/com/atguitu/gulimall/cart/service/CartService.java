package com.atguitu.gulimall.cart.service;

import com.atguitu.gulimall.cart.vo.Cart;
import com.atguitu.gulimall.cart.vo.CartItem;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface CartService {
    /**
     * 将商品添加到购物车
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车里的某个商品
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取购物车信息
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车
     */
    void clearCart(String cartKey);

    /**
     * 是否勾选商品
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 改变商品数量（点击加减）
     */
    void changeItemCount(Long skuId, Integer num);

    /**
     * 删除商品
     */
    void deleteItem(Long skuId);

    /**
     * 当前用户购物车数据
     */
    List<CartItem> getUserCartItems();

}
