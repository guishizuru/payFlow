package com.br.payFlow.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String PAYMENT_QUEUE = "payment.queue";
    public static final String PAYMENT_DLQ = "payment.dlq";
    public static final String PAYMENT_DLX = "payment.dlx";
    @Bean
    public Queue paymentQueue() {
        return QueueBuilder.durable(PAYMENT_QUEUE)
                .withArgument("x-dead-letter-exchange", PAYMENT_DLX)
                .build();
    }
    @Bean
    public Queue paymentDLQ() {
        return new Queue(PAYMENT_DLQ);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(PAYMENT_DLX);
    }

    @Bean
    public Binding dlqBinding() {
        return BindingBuilder.bind(paymentDLQ())
                .to(deadLetterExchange())
                .with(PAYMENT_QUEUE);
    }
}
