package com.atguigu.gulimall.ware.service;

import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author HeZhu
 * @email hzhu038@uottawa.ca
 * @date 2021-03-31 11:17:48
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    // 05、查询未领取的采购单
    PageUtils queryPageUnreceivePurchase(Map<String, Object> params);

    //04、合并采购需求
    void mergePurchase(MergeVo mergeVo);

    //06、领取采购单
    void received(List<Long> ids);

    //07、完成采购
    void done(PurchaseDoneVo doneVo);
}

