package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //保存sku信息
    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    //21、sku检索
    PageUtils queryPageByCondition(Map<String, Object> params);


    //2.查出 spuid -> sku信息，品牌的名字
    List<SkuInfoEntity> getSkusBySpuId(Long spuId);

    //ItemController - 查询sku详细内容
    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;

}

