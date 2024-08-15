package cn.itcast.publisher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RunWith(SpringRunner.class)
@SpringBootTest
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
}
