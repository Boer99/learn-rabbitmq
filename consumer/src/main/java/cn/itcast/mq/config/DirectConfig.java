package cn.itcast.mq.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DirectConfig {
    @Bean
    public DirectExchange directExchange2() {
//        return ExchangeBuilder.fanoutExchange("hmall.fanout2").build();
        return new DirectExchange("hmall.direct2");
    }

    @Bean
    public Queue directQueue3() {
        return new Queue("direct.queue3");
    }

    @Bean
    public Queue directQueue4() {
        return new Queue("direct.queue4");
    }

    @Bean
    public Binding bindingRed(Queue directQueue3, DirectExchange directExchange2){
        return BindingBuilder.bind(directQueue3).to(directExchange2).with("red");
    }

    @Bean
    public Binding bindingBlue(){
        return BindingBuilder.bind(directQueue3()).to(directExchange2()).with("blue");
    }
}
