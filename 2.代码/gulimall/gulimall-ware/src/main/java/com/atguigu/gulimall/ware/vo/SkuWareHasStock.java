package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

@Data
public class SkuWareHasStock {
    private Long skuId;

    private List<Long> wareId;

    private Integer num;
}
