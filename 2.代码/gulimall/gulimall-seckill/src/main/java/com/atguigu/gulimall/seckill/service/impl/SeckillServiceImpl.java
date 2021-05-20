package com.atguigu.gulimall.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.to.mq.SecKillOrderTo;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberRsepVo;
import com.atguigu.gulimall.seckill.feign.CouponFeignService;
import com.atguigu.gulimall.seckill.feign.ProductFeignService;
import com.atguigu.gulimall.seckill.intercepter.LoginUserInterceptor;
import com.atguigu.gulimall.seckill.service.SeckillService;
import com.atguigu.gulimall.seckill.to.SeckillSkuRedisTo;
import com.atguigu.gulimall.seckill.vo.SeckillSessionsWithSkus;
import com.atguigu.gulimall.seckill.vo.SkuInfoVo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String SESSION_CACHE_PREFIX = "seckill:sessions:";

    private final String SKUKILL_CACHE_PREFIX = "seckill:skus:";

    private final String SKUSTOCK_SEMAPHONE = "seckill:stock:"; // +商品随机码

    // 最近3天上架商品
    @Override
    public void uploadSeckillSkuLatest3Day() {
        // 1.扫描最近三天要参加秒杀的商品, 远程调用
        R r = couponFeignService.getLate3DaySession();
        if(r.getCode() == 0){
            List<SeckillSessionsWithSkus> sessions = r.getData(new TypeReference<List<SeckillSessionsWithSkus>>() {});
            // 2.缓存活动信息
            saveSessionInfo(sessions);
            // 3.缓存活动的关联的商品信息
            saveSessionSkuInfo(sessions);
        }
    }


    // 2.缓存活动信息
    private void saveSessionInfo(List<SeckillSessionsWithSkus> sessions){
        if(sessions != null){
            sessions.stream().forEach(session -> {
                // 开始时间
                long startTime = session.getStartTime().getTime();
                // 结束时间
                long endTime = session.getEndTime().getTime();
                // Redis key
                String key = SESSION_CACHE_PREFIX + startTime + "_" + endTime;
                Boolean hasKey = stringRedisTemplate.hasKey(key);
                if(!hasKey){
                    // 获取所有商品id
                    // 场次-skuId
                    List<String> collect = session.getRelationSkus().stream().map(item -> item.getPromotionSessionId() + "-" + item.getSkuId()).collect(Collectors.toList());
                    // 缓存活动信息
                    stringRedisTemplate.opsForList().leftPushAll(key, collect);
                }
            });
        }
    }

    // 3.缓存活动的关联的商品信息
    private void saveSessionSkuInfo(List<SeckillSessionsWithSkus> sessions){
        if(sessions != null){
            sessions.stream().forEach(session -> {
                BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                session.getRelationSkus().stream().forEach(seckillSkuVo -> {
                    // 1.商品的随机码
                    String randomCode = UUID.randomUUID().toString().replace("-", "");
                    // 防止重复上架：Redis中没有才缓存
                    if(!ops.hasKey(seckillSkuVo.getPromotionSessionId() + "-" + seckillSkuVo.getSkuId())){
                        // 2.缓存商品
                        SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                        BeanUtils.copyProperties(seckillSkuVo, redisTo);
                        // 3.sku的基本数据 sku的秒杀信息
                        R info = productFeignService.skuInfo(seckillSkuVo.getSkuId());
                        if(info.getCode() == 0){
                            SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {});
                            redisTo.setSkuInfoVo(skuInfo);
                        }
                        // 4.设置当前商品的秒杀信息
                        redisTo.setStartTime(session.getStartTime().getTime());
                        redisTo.setEndTime(session.getEndTime().getTime());

                        redisTo.setRandomCode(randomCode);
                        //场次-skuId
                        ops.put(seckillSkuVo.getPromotionSessionId() + "-" + seckillSkuVo.getSkuId(), JSON.toJSONString(redisTo));
                        // 如果当前这个场次的商品库存已经上架就不需要上架了
                        // 5.使用库存作为分布式信号量  限流
                        RSemaphore semaphore = redissonClient.getSemaphore(SKUSTOCK_SEMAPHONE + randomCode);
                        semaphore.trySetPermits(seckillSkuVo.getSeckillCount().intValue());
                    }
                });
            });
        }
    }

    /**
     * 得到当前可以参与的秒杀商品信息
     */
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {
        // 1.确定当前时间属于那个秒杀场次
        long time = new Date().getTime();
        // 定义一段受保护的资源
        // try (Entry entry = SphU.entry("seckillSkus")){
            Set<String> keys = stringRedisTemplate.keys(SESSION_CACHE_PREFIX + "*");
            for (String key : keys) {
                // seckill:sessions:1593993600000_1593995400000
                String replace = key.replace("seckill:sessions:", "");
                String[] split = replace.split("_");
                long start = Long.parseLong(split[0]);
                long end = Long.parseLong(split[1]);
                if(time >= start && time <= end){
                    // 2.获取这个秒杀场次的所有商品信息
                    List<String> range = stringRedisTemplate.opsForList().range(key, 0, 100);
                    BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
                    List<String> list = hashOps.multiGet(range);
                    if(list != null){
                        return list.stream().map(item -> {
                            SeckillSkuRedisTo redisTo = JSON.parseObject(item, SeckillSkuRedisTo.class);
						// redisTo.setRandomCode(null);
                            return redisTo;
                        }).collect(Collectors.toList());
                    }
                    break;
                }
            }
        // }catch (BlockException e){
        //     log.warn("资源被限流：" + e.getMessage());
        // }
        return null;
    }

    /**
     * 查询秒杀商品信息
     */
    @Override
    public SeckillSkuRedisTo getSkuSeckillInfo(Long skuId) {
        // 1.找到所有需要参与秒杀的商品key
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        Set<String> keys = hashOps.keys();
        // 2.遍历 keys (6_4)
        if(keys != null && keys.size() > 0){
            String regx = "\\d-" + skuId;
            for (String key : keys) {
                if(Pattern.matches(regx, key)){
                    String json = hashOps.get(key);
                    SeckillSkuRedisTo to = JSON.parseObject(json, SeckillSkuRedisTo.class);
                    // 处理一下随机码
                    long current = new Date().getTime();

                    if(current <= to.getStartTime() || current >= to.getEndTime()){
                        to.setRandomCode(null);
                    }
                    return to;
                }
            }
        }
        return null;
    }

    /**
     * 秒杀功能
     */
    @Override
    public String kill(String killId, String key, Integer num) {
        MemberRsepVo memberRsepVo = LoginUserInterceptor.threadLocal.get();

        // 1.获取当前秒杀商品的详细信息
        BoundHashOperations<String, String, String> hashOps = stringRedisTemplate.boundHashOps(SKUKILL_CACHE_PREFIX);
        String json = hashOps.get(killId);
        if(StringUtils.isEmpty(json)){
            return null;
        }else{
            //从 Redis 转换 JSON 到 SeckillSkuRedisTo
            SeckillSkuRedisTo redisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
            // 校验合法性
            long time = new Date().getTime();
            // 校验 时间 合法性
            if(time >= redisTo.getStartTime() && time <= redisTo.getEndTime()){
                String randomCode = redisTo.getRandomCode();
                String skuId = redisTo.getPromotionSessionId() + "-" + redisTo.getSkuId();
                // 1.校验 随机码 跟 商品id 是否匹配
                if(randomCode.equals(key) && killId.equals(skuId)){
                    // 2.验证 购买数量 合法
                    BigDecimal limit = redisTo.getSeckillLimit();
                    if(num <= limit.intValue()){
                        // 3.验证这个人是否已经购买过了
                        // 幂等性：秒杀成功，就去redis站位；userId_SessionId_skuId
                        String redisKey = memberRsepVo.getId() + "-" + skuId;
                        // 让数据自动过期
                        long ttl = redisTo.getEndTime() - redisTo.getStartTime();
                        // 过期 redis， 自动删除
                        Boolean aBoolean = stringRedisTemplate.opsForValue().setIfAbsent(redisKey, num.toString(), ttl<0?0:ttl, TimeUnit.MILLISECONDS);
                        // 站位成功，当前用户没有买过
                        if(aBoolean){
                            // 占位成功 说明从来没买过
                            // 分布式信号量
                            RSemaphore semaphore = redissonClient.getSemaphore(SKUSTOCK_SEMAPHONE + randomCode);
                            // 从信号量中获取，快速尝试是否获取了信号量
                            boolean acquire = semaphore.tryAcquire(num);
                            if(acquire){
                                // 秒杀成功
                                // 快速下单 发送MQ
                                String orderSn = IdWorker.getTimeId() + UUID.randomUUID().toString().replace("-","").substring(7,8);
                                SecKillOrderTo orderTo = new SecKillOrderTo();
                                //订单号
                                orderTo.setOrderSn(orderSn);
                                //用户名
                                orderTo.setMemberId(memberRsepVo.getId());
                                //购买数量
                                orderTo.setNum(num);
                                //商品
                                orderTo.setSkuId(redisTo.getSkuId());
                                //价格
                                orderTo.setSeckillPrice(redisTo.getSeckillPrice());
                                //活动场次id
                                orderTo.setPromotionSessionId(redisTo.getPromotionSessionId());
                                //发送MQ消息
                                rabbitTemplate.convertAndSend("order-event-exchange","order.seckill.order", orderTo);
                                return orderSn;
                            }
                        }else {
                            return null;
                        }
                    }
                }else{
                    return null;
                }
            }else{
                return null;
            }
        }
        return null;
    }
}










