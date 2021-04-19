package com.atguigu.gulimall.ware.service.impl;

import com.atguigu.common.constant.WareConstant;
import com.atguigu.gulimall.ware.entity.PurchaseDetailEntity;
import com.atguigu.gulimall.ware.service.PurchaseDetailService;
import com.atguigu.gulimall.ware.service.WareSkuService;
import com.atguigu.gulimall.ware.vo.MergeVo;
import com.atguigu.gulimall.ware.vo.PurchaseDoneVo;
import com.atguigu.gulimall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.ware.dao.PurchaseDao;
import com.atguigu.gulimall.ware.entity.PurchaseEntity;
import com.atguigu.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService detailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    // 05、查询未领取的采购单
    @Override
    public PageUtils queryPageUnreceivePurchase(Map<String, Object> params) {
        //status 是 0 或者 1 的，是未采购的
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    //04、合并采购需求：多个采购需求 --(合并)--> 一个采购单
    @Transactional
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        // 如果没有采购单，则新建一个
        // 如果有采购单，则合并
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId == null){
            //1、新建一个
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            //设置默认值
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            this.save(purchaseEntity);
            //得到当前采购单的id
            purchaseId = purchaseEntity.getId();
        }

        //TODO 确认采购单状态是0,1才可以合并

        //得到需要整合的订单
        List<Long> items = mergeVo.getItems();
        //最终采购单的id：多个采购需求 --(合并)--> 一个采购单
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> collect = items.stream().map(i -> {
            //采购需求
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            //更改采购需求
            detailEntity.setId(i);
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());

        //将更新的采购需求录入数据库
        detailService.updateBatchById(collect);

        //更新时间
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchaseId);
        purchaseEntity.setUpdateTime(new Date());
        this.updateById(purchaseEntity);
    }

    //06、领取采购单
    //前端输入：采购单id
    @Override
    public void received(List<Long> ids) {
        //1.领取采购单，更新其状态为 已领取
        List<PurchaseEntity> collect = ids.stream().map(id -> {
            //1.1 得到采购订单
            PurchaseEntity byId = this.getById(id);
            return byId;
        }).filter(item -> {
            //1.2 采购单 [新建 or 已分配] 才可以领取
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map(item->{
            //1.3 改变 采购单状态 为 已领取
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return item;
        }).collect(Collectors.toList());

        //更新数据库
        this.updateBatchById(collect);


        //3.改变 采购项状态
        //根据 purchaseId -> 所有采购需求 -> 改变 采购项状态
        collect.forEach((item)->{
            //3.1 得到所有需要更新状态的 采购需求
            List<PurchaseDetailEntity> entities = detailService.listDetailByPurchaseId(item.getId());
            //3.2 仅仅更新 采购需求表 中 status 状态为 正在购买
            List<PurchaseDetailEntity> detailEntities = entities.stream().map(entity -> {
                PurchaseDetailEntity entity1 = new PurchaseDetailEntity();
                entity1.setId(entity.getId());
                entity1.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return entity1;
            }).collect(Collectors.toList());
            detailService.updateBatchById(detailEntities);
        });
    }

    //07、完成采购
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {
        Long id = doneVo.getId();
        //1、改变 采购需求 的状态
        Boolean flag = true;
        List<PurchaseItemDoneVo> items = doneVo.getItems();

        //需要更新状态的 采购需求
        List<PurchaseDetailEntity> updates = new ArrayList<>();
        //遍历所有采购需求
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            //如果那个 采购需求 失败，则维持原来状态
            if(item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                flag = false;
                detailEntity.setStatus(item.getStatus());
            }
            //更新状态为 采购完成 的 采购需求
            else{
                detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());

                //3、将成功采购的进行入库：（当前采购商品id，采购仓库id，采购数量）
                //根据 采购需求id 查出 其实体类所有信息（采购仓库id，采购数量 ...）
                PurchaseDetailEntity entity = detailService.getById(item.getItemId());
                //采购入库（当前采购商品id，采购仓库id，采购数量）
                wareSkuService.addStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum());
            }
            //设置需要更新的 采购需求 的对应实体类（id 和 状态），并将其添加进 update（所有 需要更新的 采购需求）
            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        //更新数据库
        detailService.updateBatchById(updates);

        //2、改变 采购单 状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        //如果 所有 采购需求 都完成，则采购单状态为 完成
        purchaseEntity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setUpdateTime(new Date());
        //更新采购单 进数据库
        this.updateById(purchaseEntity);
    }

}