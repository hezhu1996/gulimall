package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.Catelog2Vo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //查出所有分类并组装成树形结构
    List<CategoryEntity> listWithTree();

    //1.检查当前删除菜单，是否被别的地方引用
    void removeMenuByIds(List<Long> asList);

    //根据catId，查询CatelogPath完整路径
    Long[] findCatelogPath(Long catelogId);

    //1.因为有categoryRelation的冗余存储，不能光改品牌表，所有冗余存储都要更新
    void updateCascade(CategoryEntity category);

    //web 1. 查出所有1级分类
    List<CategoryEntity> getLevel1Categorys();

    //web 2.得到二级、三级子分类
    Map<String, List<Catelog2Vo>> getCatelogJson();

}

