package com.atguigu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.AttrEntity;

import java.util.Map;

/**
 * 商品属性
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-04-02 20:50:01
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

