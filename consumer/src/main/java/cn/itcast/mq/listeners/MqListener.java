package cn.itcast.mq.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqListener {
    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String msg) {
        log.info("消费者收到了simple.queue的消息: {}", msg);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueueMessage1(String msg) throws InterruptedException {
        log.info("消费者收到了work.queue的消息: {}", msg);
        Thread.sleep(20);
    }

    @RabbitListener(queues = "work.queue")
    public void listenWorkQueueMessage2(String msg) throws InterruptedException {
        log.error("消费者收到了work.queue的消息: {}", msg);
        Thread.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueueMessage1(String msg) {
        log.info("消费者收到了fanout.queue的消息: {}", msg);
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueueMessage2(String msg) {
        log.error("消费者收到了fanout.queue的消息: {}", msg);
    }

    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueueMessage1(String msg) {
        log.info("消费者收到了direct.queue1的消息: {}", msg);
    }

    @RabbitListener(queues = "direct.queue2")
    public void listenDirectQueueMessage2(String msg) {
        log.error("消费者收到了direct.queue2的消息: {}", msg);
    }

    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueueMessage1(String msg) {
        log.info("消费者收到了topic.queue1的消息: {}", msg);
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueueMessage2(String msg) {
        log.error("消费者收到了topic.queue2的消息: {}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.ann.q1", durable = "true"),
            exchange = @Exchange(name = "hmall.ann.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}
    ))
    public void listenDirectAnnQ1(String msg) {
        log.info("消费者收到了direct.ann.q1的消息: {}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.ann.q2", durable = "true"),
            exchange = @Exchange(name = "hmall.ann.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}
    ))
    public void listenDirectAnnQ2(String msg) {
        log.info("消费者收到了direct.ann.q2的消息: {}", msg);
    }
}
