package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //16、新增品牌与分类关联关系
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    //2.更新其他存有"品牌名的表"
    //2.1 更新 品牌关联 表
    void updateBrand(Long brandId, String name);

    //2.更新其他存有"分类名的表"
    //2.1 更新 分类关联 表
    void updateCategory(Long catId, String name);

    //14、获取分类关联的品牌
    List<BrandEntity> getBrandByCatId(Long catId);
}

