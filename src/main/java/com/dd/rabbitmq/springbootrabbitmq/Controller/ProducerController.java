package com.dd.rabbitmq.springbootrabbitmq.Controller;

import com.dd.rabbitmq.springbootrabbitmq.config.MyCallBack;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/confirm")
@Slf4j
public class ProducerController {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MyCallBack myCallBack;

    /**
     * 依赖注入 rabbitTemplate 之后再设置它的回调对象 否则不会调用自定义的回调方法
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(myCallBack);
        rabbitTemplate.setReturnCallback(myCallBack);
    }

    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {
        //指定消息 id 为 1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey = "key1";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData1);
        /**
         * 这里配置了备份交换机（alternate）之后 该不可路由的消息会被confirmExchange转发到备份交换机中
         * 再由备份交换机将该消息发送给warning队列进行警告 以及 backup队列进行备份
         */
        CorrelationData correlationData2 = new CorrelationData("2");
        routingKey = "key2";
        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, message + routingKey, correlationData2);
        log.info("发送消息内容:{}", message);

    }
}
