package com.br.payFlow;

import com.br.payFlow.dto.request.PaymentRequestDTO;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.gateway.FakePaymentGateway;
import com.br.payFlow.repository.PaymentRepository;
import com.br.payFlow.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentGatewayTest {

    @Mock
    private PaymentRepository repository;

    @InjectMocks
    private PaymentService service;

    @Test
    void shouldThrowException() {
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(100)).thenReturn(95);

        FakePaymentGateway gateway = new FakePaymentGateway(mockRandom);

        assertThrows(RuntimeException.class, () -> {
            gateway.process(new Payment());
        });
    }
    @Test
    void shouldReturnApproved() {
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(100)).thenReturn(50);

        FakePaymentGateway gateway = new FakePaymentGateway(mockRandom);

        boolean result = gateway.process(new Payment());

        assertTrue(result);
    }
    @Test
    void shouldReturnFailed() {
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextInt(100)).thenReturn(80);

        FakePaymentGateway gateway = new FakePaymentGateway(mockRandom);

        boolean result = gateway.process(new Payment());

        assertFalse(result);
    }
    @Test
    void shouldRecoverAfterDataIntegrityViolation() {

        String externalId = "123";

        Payment payment = new Payment();
        payment.setExternalId(externalId);

        when(repository.findByExternalId(externalId))
                .thenReturn(Optional.empty())
                .thenReturn(Optional.of(payment));

        when(repository.save(any()))
                .thenThrow(new DataIntegrityViolationException("erro"));

        Payment result = service.createPayment(
                new PaymentRequestDTO(BigDecimal.valueOf(100.00), "Teste"),
                externalId
        );

        assertEquals(externalId, result.getExternalId());
    }
}
