package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:07:12
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
