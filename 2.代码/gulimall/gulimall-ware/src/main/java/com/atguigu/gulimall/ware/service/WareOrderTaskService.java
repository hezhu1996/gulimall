package com.atguigu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.WareOrderTaskEntity;

import java.util.Map;

/**
 * 库存工作单
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-03-31 11:17:48
 */
public interface WareOrderTaskService extends IService<WareOrderTaskEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查找库存工作单
     */
    WareOrderTaskEntity getOrderTaskByOrderSn(String orderSn);
}

