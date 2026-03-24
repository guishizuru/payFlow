package com.br.payFlow.messaging.producer;


import com.br.payFlow.config.RabbitConfig;
import com.br.payFlow.entity.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendPayment(Payment payment) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.PAYMENT_QUEUE,
                payment
        );
    }
}
