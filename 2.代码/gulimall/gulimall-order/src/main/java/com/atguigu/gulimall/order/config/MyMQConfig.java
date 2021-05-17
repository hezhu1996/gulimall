package com.atguigu.gulimall.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MyMQConfig {
    @Value("${myRabbitmq.MQConfig.queues}")
    private String queues;

    @Value("${myRabbitmq.MQConfig.eventExchange}")
    private String eventExchange;

    @Value("${myRabbitmq.MQConfig.routingKey}")
    private String routingKey;

    @Value("${myRabbitmq.MQConfig.delayQueue}")
    private String delayQueue;

    @Value("${myRabbitmq.MQConfig.createOrder}")
    private String createOrder;

    @Value("${myRabbitmq.MQConfig.ReleaseOther}")
    private String ReleaseOther;

    @Value("${myRabbitmq.MQConfig.ReleaseOtherKey}")
    private String ReleaseOtherKey;

    @Value("${myRabbitmq.MQConfig.ttl}")
    private Integer ttl;

    /**
     * 第一次创建
     */
    // @RabbitListener(queues = "order.release.order.queue")
    // public void handle(Message message) {
    //
    // }

    /**
     * 延时队列1
     */
    @Bean
    public Queue orderDelayQueue(){
        Map<String ,Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", eventExchange);
        arguments.put("x-dead-letter-routing-key", routingKey);
        arguments.put("x-message-ttl", ttl);
        Queue queue = new Queue(delayQueue, true, false, false, arguments);
        return queue;
    }

    /**
     * 延时队列2
     */
    @Bean
    public Queue orderReleaseOrderQueue(){
        Queue queue = new Queue(queues, true, false, false);
        return queue;
    }

    /**
     * 交换机
     */
    @Bean
    public Exchange orderEventExchange(){

        return new TopicExchange(eventExchange, true, false);
    }

    /**
     * 与延时队列1 绑定
     */
    @Bean
    public Binding orderCreateOrderBinding(){

        return new Binding(delayQueue, Binding.DestinationType.QUEUE, eventExchange, createOrder, null);
    }

    /**
     * 与延时队列2 绑定
     */
    @Bean
    public Binding orderReleaseOrderBinding(){

        return new Binding(queues, Binding.DestinationType.QUEUE, eventExchange, routingKey, null);
    }

    /**
     * 订单释放直接和库存释放进行绑定
     */
    @Bean
    public Binding orderReleaseOtherBinding(){

        return new Binding(ReleaseOther, Binding.DestinationType.QUEUE, eventExchange, ReleaseOtherKey + ".#", null);
    }

    @Bean
    public Queue orderSecKillQueue(){
        return new Queue("order.seckill.order.queue", true, false, false);
    }

    @Bean
    public Binding orderSecKillQueueBinding(){
        return new Binding("order.seckill.order.queue", Binding.DestinationType.QUEUE, "order-event-exchange", "order.seckill.order", null);
    }
}
