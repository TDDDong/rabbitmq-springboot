package com.dd.rabbitmq.springbootrabbitmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 告警队列 用于接收备份交换机中 不可路由的消息 进行告警
 * 同时配置了 无法路由到队列的回调方法（returnCallBack） 和 备份交换机（alternate）的处理方法  备份交换机的优先级更高!!!!!
 */
@Slf4j
@Component
public class WarningConsumer {
    public static final String WARNING_QUEUE_NAME = "warning.queue";
    @RabbitListener(queues = WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) {
        String msg = new String(message.getBody());
        log.error("报警发现不可路由消息：{}", msg);
    }
}
