package cn.itcast.mq.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqListener {
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) {
        log.info("消费者收到了simple.queue的消息: {}", msg);
    }
}
