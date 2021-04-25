package com.atguigu.common.to.es;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuEsModel implements Serializable {
    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hotScore;

    private Long brandId;

    private Long catalogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    private List<Attrs> attrs;

    /**
     *  检索属性
     */
    @Data
    public static class Attrs implements Serializable {
        private Long attrId;

        private String attrName;

        private String attrValue;
    }
}
