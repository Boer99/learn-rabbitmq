package cn.itcast.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfig {
    @Bean
    public FanoutExchange fanoutExchange2() {
//        return ExchangeBuilder.fanoutExchange("hmall.fanout2").build();
        return new FanoutExchange("hmall.fanout2");
    }

    @Bean
    public Queue fanoutQueue3() {
        return new Queue("fanout.queue3");
    }

    @Bean
    public Queue fanoutQueue4() {
        return new Queue("fanout.queue4");
    }

    @Bean
    public Binding bindingQueue1(Queue fanoutQueue3, FanoutExchange fanoutExchange2){
        return BindingBuilder.bind(fanoutQueue3).to(fanoutExchange2);
    }

    @Bean
    public Binding bindingQueue2(){
        return BindingBuilder.bind(fanoutQueue4()).to(fanoutExchange2());
    }
}
