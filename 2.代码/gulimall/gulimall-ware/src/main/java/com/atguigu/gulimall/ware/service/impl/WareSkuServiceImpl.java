package com.atguigu.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.enume.OrderStatusEnum;
import com.atguigu.common.exception.NotStockException;
import com.atguigu.common.to.SkuHasStockVo;
import com.atguigu.common.to.mq.OrderTo;
import com.atguigu.common.to.mq.StockDetailTo;
import com.atguigu.common.to.mq.StockLockedTo;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.atguigu.gulimall.ware.entity.WareOrderTaskEntity;
import com.atguigu.gulimall.ware.feign.OrderFeignService;
import com.atguigu.gulimall.ware.feign.ProductFeignService;
import com.atguigu.gulimall.ware.service.WareOrderTaskDetailService;
import com.atguigu.gulimall.ware.service.WareOrderTaskService;
import com.atguigu.gulimall.ware.vo.OrderItemVo;
import com.atguigu.gulimall.ware.vo.OrderVo;
import com.atguigu.gulimall.ware.vo.SkuWareHasStock;
import com.atguigu.gulimall.ware.vo.WareSkuLockVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.ware.dao.WareSkuDao;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.atguigu.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Slf4j
@RabbitListener(queues = "stock.release.stock.queue")
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Autowired
    ProductFeignService productFeignService;

    @Autowired
    private WareOrderTaskService orderTaskService;

    @Autowired
    private WareOrderTaskDetailService orderTaskDetailService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderFeignService orderFeignService;

    @Value("${myRabbitmq.MQConfig.eventExchange}")
    private String eventExchange;

    @Value("${myRabbitmq.MQConfig.routingKey}")
    private String routingKey;

    //02、查询商品库存
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId: 1
         * wareId: 2
         */
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }

        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }


        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    //采购入库（当前采购商品id，采购仓库id，采购数量）
    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1、判断如果 当前商品 还没有这个库存记录，新增
        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(entities == null || entities.size() == 0){
            WareSkuEntity skuEntity = new WareSkuEntity();
            skuEntity.setSkuId(skuId);
            skuEntity.setStock(skuNum);
            skuEntity.setWareId(wareId);
            skuEntity.setStockLocked(0);
            //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
            //1、自己catch异常
            //TODO 还可以用什么办法让异常出现以后不回滚？高级
            try {
                //远程调用product，根据商品id，查询商品名称
                R info = productFeignService.info(skuId);
                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");

                //如果查询成功，则设置商品名称
                if(info.getCode() == 0){
                    skuEntity.setSkuName((String) data.get("skuName"));
                }
            }catch (Exception e){

            }
            //新增入库
            wareSkuDao.insert(skuEntity);
        }
        //2. 如果有，修改操作
        else{
            //更新 wms_ware_sku
            //sql: UPDATE `wms_ware_sku` SET stock=stock+#{skuNum} WHERE sku_id=#{skuId} AND ware_id=#{wareId}
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }
    }

    //查询sku是否有库存
    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> skuHasStockVos = skuIds.stream().map(skuId -> {
            SkuHasStockVo vo = new SkuHasStockVo();
            //查询sku总库存量
            Long count = baseMapper.getSkuStock(skuId);
            vo.setSkuId(skuId);
            vo.setHasStock(count ==null?false:count>0);
            return vo;
        }).collect(Collectors.toList());

        return skuHasStockVos;
    }

    /**
     * 锁定库存
     */
    @Transactional(rollbackFor = NotStockException.class)
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {
        // 当定库存之前先保存订单 以便后来消息撤回
        WareOrderTaskEntity taskEntity = new WareOrderTaskEntity();
        taskEntity.setOrderSn(vo.getOrderSn());
        orderTaskService.save(taskEntity);
        // [理论上]1. 按照下单的收获地址 找到一个就近仓库, 锁定库存
        // [实际上]1. 找到每一个商品在那个一个仓库有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map(item -> {
            SkuWareHasStock hasStock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            hasStock.setSkuId(skuId);
            // 查询这个商品在哪有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            hasStock.setWareId(wareIds);
            hasStock.setNum(item.getCount());
            return hasStock;
        }).collect(Collectors.toList());
        //2. 锁定库存，遍历每一个商品
        for (SkuWareHasStock hasStock : collect) {
            //Flag：当前商品是否锁住
            Boolean skuStocked = true;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            // 商品无货。没有任何仓库有这个库存，直接退出
            if(wareIds == null || wareIds.size() == 0){
                throw new NotStockException(skuId.toString());
            }
            // 商品有货
            // 如果每一个商品都锁定成功 将当前商品锁定了几件的工作单记录发送给MQ
            // 如果锁定失败 前面保存的工作单信息回滚了
            for (Long wareId : wareIds) {
                // 锁定库存：成功就返回， 1 失败返回0 （mysql几行影响successful）
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if(count == 1){
                    // TODO 告诉MQ库存锁定成功 一个订单锁定成功 消息队列就会有一个消息
                    WareOrderTaskDetailEntity detailEntity = new WareOrderTaskDetailEntity(
                                                                                            null,
                                                                                            skuId,
                                                                                            "",
                                                                                            hasStock.getNum() ,
                                                                                            taskEntity.getId(),
                                                                                            wareId,
                                                                                            1);
                    // 保存进数据库
                    orderTaskDetailService.save(detailEntity);
                    // 工作单id, 锁定库存id
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(taskEntity.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(detailEntity, detailTo);
                    // 防止回滚以后找不到数据 把详细信息页
                    stockLockedTo.setDetailTo(detailTo);
                    // 延时队列
                    rabbitTemplate.convertAndSend(eventExchange, routingKey ,stockLockedTo);
                    skuStocked = false;
                    break;
                }
                // 当前仓库锁定失败 重试下一个仓库
            }
            if(skuStocked){
                // 当前商品在所有仓库都没锁柱
                throw new NotStockException(skuId.toString());
            }
        }
        // 3.全部锁定成功
        return true;
    }

    /**
     * 解锁库存
     */
    @Override
    public void unlockStock(StockLockedTo to) {
        log.info("\n收到解锁库存的消息");
        // 库存id
        Long id = to.getId();
        StockDetailTo detailTo = to.getDetailTo();
        Long detailId = detailTo.getId();
        /**
         * 解锁库存
         * 	查询数据库关系这个订单的详情
         * 		有: 证明库存锁定成功
         * 			1.没有这个订单, 必须解锁
         * 			2.有这个订单 可能需要解锁库存
         * 				订单状态：已取消,解锁库存
         * 				没取消：不能解锁	;
         * 		没有：就是库存锁定失败， 库存回滚了 这种情况无需回滚
         */
        WareOrderTaskDetailEntity byId = orderTaskDetailService.getById(detailId);
        if(byId != null){
            // 解锁
            WareOrderTaskEntity taskEntity = orderTaskService.getById(id);
            String orderSn = taskEntity.getOrderSn();
            // 根据订单号 查询订单状态 已取消才解锁库存
            R orderStatus = orderFeignService.getOrderStatus(orderSn);
            if(orderStatus.getCode() == 0){
                // 订单数据返回成功
                OrderVo orderVo = orderStatus.getData(new TypeReference<OrderVo>() {});
                // 订单不存在
                if(orderVo == null || orderVo.getStatus() == OrderStatusEnum.CANCLED.getCode()){
                    // 订单已取消 状态1 已锁定  这样才可以解锁
                    if(byId.getLockStatus() == 1){
                        unLock(detailTo.getSkuId(), detailTo.getWareId(), detailTo.getSkuNum(), detailId);
                    }
                }
            }else{
                // 消息拒绝 重新放回队列 让别人继续消费解锁
                throw new RuntimeException("远程服务失败");
            }
        }else{
            // 无需解锁
        }
    }

    /**
     * 数据库实施解锁库存
     */
    private void unLock(Long skuId, Long wareId, Integer num, Long taskDeailId){
        // 更新库存
        wareSkuDao.unlockStock(skuId, wareId, num);
        // 更新库存工作单的状态
        WareOrderTaskDetailEntity detailEntity = new WareOrderTaskDetailEntity();
        detailEntity.setId(taskDeailId);
        detailEntity.setLockStatus(2);
        orderTaskDetailService.updateById(detailEntity);
    }

    /**
     * 防止订单服务卡顿 导致订单状态一直改不了 库存消息有限到期 最后导致卡顿的订单 永远无法解锁库存
     */
    @Transactional
    @Override
    public void unlockStock(OrderTo to) {
        log.info("\n订单超时自动关闭,准备解锁库存");
        String orderSn = to.getOrderSn();
        // 查一下最新的库存状态 防止重复解锁库存[Order服务可能会提前解锁]
        WareOrderTaskEntity taskEntity = orderTaskService.getOrderTaskByOrderSn(orderSn);
        Long taskEntityId = taskEntity.getId();
        // 按照工作单找到所有 没有解锁的库存 进行解锁 状态为1等于已锁定
        List<WareOrderTaskDetailEntity> entities = orderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>().eq("task_id", taskEntityId).eq("lock_status", 1));
        for (WareOrderTaskDetailEntity entity : entities) {
            // 解锁
            unLock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum(), entity.getId());
        }
    }
}



















