/**
 * Copyright 2019 bejson.com
 */
package com.atguigu.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Auto-generated: 2019-11-26 10:50:34
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class SpuSaveVo {
    //基本信息
    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;

    //描述
    private List<String> decript;
    //图片
    private List<String> images;
    private Bounds bounds;

    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;



}