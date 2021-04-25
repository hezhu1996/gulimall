package com.atguigu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2Vo {
    private String id;

    private String name;

    private String catalog1Id;

    private List<Catalog3Vo> catalog3List;
}
