package cn.itcast.publisher;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringAMQPTest {

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testSendMessage2SimpleQueue() {
        String queueName = "simple.queue";
        String message = "hello, spring amqp2!";
        rabbitTemplate.convertAndSend(queueName, message);
    }

    @Test
    public void testSendMessage2WorkQueue() throws InterruptedException {
        String queueName = "work.queue";
        for (int i = 1; i <= 50; i++) {
            String message = "hello, work.queue---" + i;
            rabbitTemplate.convertAndSend(queueName, message);
            Thread.sleep(20);
        }
    }

    @Test
    public void testSendMessage2FanoutQueue() {
        String exchangeName = "hmall.fanout";
        String queueName = "fanout.queue";
        String message = "hello, everyone!";
        rabbitTemplate.convertAndSend(exchangeName, queueName, message);
    }

    @Test
    public void testSendMessage2DirectQueue() {
        String exchangeName = "hmall.direct";
        rabbitTemplate.convertAndSend(exchangeName, "red", "hello red!");
        rabbitTemplate.convertAndSend(exchangeName, "blue", "hello blue!");
        rabbitTemplate.convertAndSend(exchangeName, "yellow", "hello yellow!");
    }

    @Test
    public void testSendMessage2TopicQueue() {
        String exchangeName = "hmall.topic";
        rabbitTemplate.convertAndSend(exchangeName, "china.news", "hello china.news!");
        rabbitTemplate.convertAndSend(exchangeName, "japan.news", "hello japan.news!");
    }

    @Test
    public void testSendMessage2ObjectQueue() {
        Map<String, Object> jack = new HashMap<>();
        jack.put("name", "jack");
        jack.put("age", 18);
        rabbitTemplate.convertAndSend("object.q", jack);
    }

    @Test
    public void testConfirmCallback() throws InterruptedException {
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            // spring内部的失败，几乎不会发生，不用管
            @Override
            public void onFailure(Throwable throwable) {
                log.error("消息回调失败: {}", throwable.getMessage());
            }

            // 回调成功
            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                log.info("收到confirm callback回调");
                if (confirm.isAck()) {
                    log.info("消息投递成功，收到ack");
                } else {
                    log.error("消息投递失败，收nack，原因：{}", confirm.getReason());
                }
            }
        });
//        rabbitTemplate.convertAndSend("hmall.direct", "red", "测试确认机制", cd);
        rabbitTemplate.convertAndSend("hmall.direct", "不存在的routingKey", "测试确认机制", cd);
        Thread.sleep(2000); // JVM直接销毁了看不到，需要睡眠一下
    }

    /**
     * 100k消息放在内存，让消息挤压，观察paged out
     */
    @Test
    public void testPageOut1() {
        Message message = MessageBuilder
                .withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();
        for (int i = 0; i < 2000000; i++) {
            rabbitTemplate.convertAndSend("simple.queue", message);
        }
    }

    /**
     * 100k消息都持久化，观察paged out
     */
    @Test
    public void testPageOut2() {
        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("simple.queue", "hello");
        }
    }
}

