package com.atguigu.gulimall.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MyRabbitConfig {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    //转为JSON存储消息
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct
    public void initRabbitTemplate(){

        // 设置确认回调
        // Confirm callback
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {

            /**
             * 只要消息抵达broker，就 ack = true
             * @param correlationData 当前消息的唯一关联数据（此消息唯一id）
             * @param ack 消息是否成功收到
             * @param cause 失败的原因
             */
            //服务器收到
            //1. 做好消息确认机制 (publisher, consumer 【手动ack】)
            //2. 每个发送的消息都在数据库做好记录，定期将失败的消息再次发送一遍
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("confirm callback");
            }
        });

        // 设置消息抵达队列的确认回调
        // Return callback
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要消息没有投递给指定队列，就触发这个失败回调
             * @param message 投递失败的详细信息
             * @param replyCode 回复的状态码
             * @param replyText 回复的文本内容
             * @param exchange 当时这个消息发给哪个交换机
             * @param routingKey 当时这个消息用的哪个路由键
             */
            // 只要消息没有投递到指定队列，就触发这个失败回调
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                //报错，修改数据库当前消息的错误状态 -> 错误
                System.out.println("return callback");
            }
        });
    }


}
