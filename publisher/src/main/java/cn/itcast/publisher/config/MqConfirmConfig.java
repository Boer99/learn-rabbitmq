package cn.itcast.publisher.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class MqConfirmConfig implements ApplicationContextAware {
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RabbitTemplate template = applicationContext.getBean(RabbitTemplate.class);
        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) ->
                log.info("路由失败，收到消息的return callback，exchange：{}，routingKey：{}，message：{}，replyCode：{}，replyText：{}",
                exchange, routingKey, message, replyCode, replyText));
    }
}
