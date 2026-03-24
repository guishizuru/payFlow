package com.br.payFlow.messaging.consumer;

import com.br.payFlow.config.RabbitConfig;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.entity.PaymentStatus;
import com.br.payFlow.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {

    private final PaymentRepository repository;

    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
    public void processPayment(Payment payment) {

        boolean approved = new Random().nextBoolean();

        Payment existing = repository.findById(payment.getId()).orElseThrow();

        existing.setStatus(approved ? PaymentStatus.APPROVED : PaymentStatus.FAILED);
        existing.setUpdatedAt(LocalDateTime.now());

        repository.save(existing);
    }
}
