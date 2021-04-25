package com.atguigu.gulimall.ware.service;

import com.atguigu.common.to.SkuHasStockVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-03-31 11:17:48
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //采购入库（当前采购商品id，采购仓库id，采购数量）
    void addStock(Long skuId, Long wareId, Integer skuNum);

    //查询sku是否有库存
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

