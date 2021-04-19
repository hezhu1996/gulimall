package com.atguigu.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-03-31 11:17:48
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    //03、查询采购需求
    PageUtils queryPage(Map<String, Object> params);

    // 得到所有需要更新状态的 采购需求
    List<PurchaseDetailEntity> listDetailByPurchaseId(Long id);
}

