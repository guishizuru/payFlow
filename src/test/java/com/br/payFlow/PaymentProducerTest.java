package com.br.payFlow;

import com.br.payFlow.entity.Payment;
import com.br.payFlow.messaging.producer.PaymentProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PaymentProducerTest {
    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentProducer producer;

    @Test
    void shouldSendMessage() {

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());

        producer.sendPayment(payment);

        verify(rabbitTemplate).convertAndSend(anyString(), Optional.ofNullable(any()));
    }
}
