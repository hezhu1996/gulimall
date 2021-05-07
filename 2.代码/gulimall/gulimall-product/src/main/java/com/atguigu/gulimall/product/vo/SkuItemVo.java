package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {
    /**
     * 基本信息 pms_sku_info
     */
    SkuInfoEntity info;

    boolean hasStock = true;

    /**
     * 图片信息 pms_sku_images
     */
    List<SkuImagesEntity> images;

    /**
     * 销售属性组合
     * 1.颜色
     * 2.版本
     * 3.增值保障
     * 。。。
     */
    List<ItemSaleAttrVo> saleAttr;

    /**
     * 介绍
     */
    SpuInfoDescEntity desc;

    /**
     * 参数规格信息
     */
    List<SpuItemAttrGroup> groupAttrs;

    /**
     * 秒杀信息
     */
    SeckillInfoVo seckillInfoVo;
}
