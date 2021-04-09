package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-03-31 11:17:48
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
