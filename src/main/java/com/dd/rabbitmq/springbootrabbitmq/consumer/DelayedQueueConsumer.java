package com.dd.rabbitmq.springbootrabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@Slf4j
@Component
public class DelayedQueueConsumer {
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    @RabbitListener(queues = DELAYED_QUEUE_NAME)
    public void receiveDelayedQueue(Message message){
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到延时队列的消息：{}", new Date().toString(), msg);
    }
}
