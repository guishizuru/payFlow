package com.br.payFlow;

import com.br.payFlow.entity.Payment;
import com.br.payFlow.entity.PaymentStatus;
import com.br.payFlow.gateway.FakePaymentGateway;
import com.br.payFlow.gateway.PaymentGateway;
import com.br.payFlow.messaging.consumer.PaymentConsumer;
import com.br.payFlow.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentConsumerTest {
    @Mock
    private PaymentRepository repository;

    @Mock
    private PaymentGateway gateway;

    @InjectMocks
    private PaymentConsumer consumer;

    @Test
    void shouldApprovePayment() {

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());

        when(repository.findById(payment.getId()))
                .thenReturn(Optional.of(payment));

        when(gateway.process(any())).thenReturn(true);

        consumer.processPayment(payment);

        assertEquals(PaymentStatus.APPROVED, payment.getStatus());
        verify(repository).save(payment);
    }
    @Test
    void shouldReturnTrue() {
        FakePaymentGateway gateway = new FakePaymentGateway();

        boolean result = gateway.process(new Payment());

        assertNotNull(result);
    }
    @Test
    void shouldThrowExceptionWhenGatewayFails() {

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());

        when(repository.findById(payment.getId()))
                .thenReturn(Optional.of(payment));

        when(gateway.process(any()))
                .thenThrow(new RuntimeException("Erro gateway"));

        assertThrows(RuntimeException.class, () -> {
            consumer.processPayment(payment);
        });
    }
    @Test
    void shouldFailPayment() {

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());

        when(repository.findById(payment.getId()))
                .thenReturn(Optional.of(payment));

        when(gateway.process(any())).thenReturn(false);

        consumer.processPayment(payment);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }
    @Test
    void shouldProcessDLQAndMarkAsFailed() {

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());

        when(repository.findById(payment.getId()))
                .thenReturn(Optional.of(payment));

        consumer.processDLQ(payment);

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        verify(repository).save(payment);
    }
}
