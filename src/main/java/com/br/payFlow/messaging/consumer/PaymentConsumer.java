package com.br.payFlow.messaging.consumer;

import com.br.payFlow.config.RabbitConfig;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.entity.PaymentStatus;
import com.br.payFlow.gateway.FakePaymentGateway;
import com.br.payFlow.gateway.PaymentGateway;
import com.br.payFlow.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final PaymentRepository repository;
    private final PaymentGateway paymentGateway;

    @RabbitListener(queues = RabbitConfig.PAYMENT_QUEUE)
    public void processPayment(Payment payment) {

        Payment existing = repository.findById(payment.getId()).orElseThrow();

        log.info("Processando pagamento. id={}", payment.getId());
        boolean approved = paymentGateway.process(existing);

        existing.setStatus(approved ? PaymentStatus.APPROVED : PaymentStatus.FAILED);
        existing.setUpdatedAt(LocalDateTime.now());


        repository.save(existing);

        log.info("Pagamento atualizado. id={} status={}",
                existing.getId(), existing.getStatus());
    }
    @RabbitListener(queues = "payment.dlq")
    public void processDLQ(Payment payment) {
        log.error("Pagamento falhou após retries. id={} status={}",
                payment.getId(), payment.getStatus());

        Payment existing = repository.findById(payment.getId()).orElseThrow();

        existing.setStatus(PaymentStatus.FAILED);
        existing.setUpdatedAt(LocalDateTime.now());

        repository.save(existing);
        log.info("Pagamento atualizado com Erro. id={} status={}",
                existing.getId(), existing.getStatus());
    }
}
