package com.atguigu.gulimall.order.dao;

import com.atguigu.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 21:08:43
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
