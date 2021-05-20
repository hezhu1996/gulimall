package com.atguigu.gulimall.order.service;

import com.atguigu.common.to.mq.SecKillOrderTo;
import com.atguigu.gulimall.order.vo.*;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.order.entity.OrderEntity;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:08:43
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 跳转到订单页，并返回订单确认页需要的数据
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    /**
     * 下单
     */
    SubmitOrderResponseVo submitOrder(OrderSubmitVo submitVo);

    /**
     * 查询订单状态
     */
    OrderEntity getOrderByOrderSn(String orderSn);

    // 关闭订单
    void closeOrder(OrderEntity entity);

    // 获取当前订单的支付信息
    PayVo getOrderPay(String orderSn);

    /**
     * 查询当前登录的用户的所有订单信息
     */
    PageUtils queryPageWithItem(Map<String, Object> params);

    /**
     * 处理支付宝返回数据
     */
    String handlePayResult(PayAsyncVo vo);

    // 创建秒杀单的信息
    void createSecKillOrder(SecKillOrderTo secKillOrderTo);
}

