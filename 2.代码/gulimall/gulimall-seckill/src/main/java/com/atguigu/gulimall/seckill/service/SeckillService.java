package com.atguigu.gulimall.seckill.service;

import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

public interface SeckillService {
    // 最近3天上架商品
    void uploadSeckillSkuLatest3Day();

    /**
     * 得到当前可以参与的秒杀商品信息
     */
    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    /**
     * 查询秒杀商品信息
     */
    SeckillSkuRedisTo getSkuSeckillInfo(Long skuId);

    /**
     * 秒杀功能
     */
    String kill(String killId, String key, Integer num);
}
