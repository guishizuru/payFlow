package com.br.payFlow;

import com.br.payFlow.dto.request.PaymentRequestDTO;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.gateway.FakePaymentGateway;
import com.br.payFlow.messaging.producer.PaymentProducer;
import com.br.payFlow.repository.PaymentRepository;
import com.br.payFlow.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository repository;
    @Mock
    private PaymentProducer producer;
    @InjectMocks
    private PaymentService service;
    @Test
    void shouldCreatePaymentWhenExternalIdNotExists() {

        String externalId = "123";
        PaymentRequestDTO request = new PaymentRequestDTO(BigDecimal.valueOf(100.00), "Teste");

        when(repository.findByExternalId(externalId)).thenReturn(Optional.empty());

        Payment saved = new Payment();
        saved.setId(UUID.randomUUID());

        when(repository.save(any())).thenReturn(saved);

        Payment result = service.createPayment(request, externalId);

        assertNotNull(result);
        verify(repository).save(any());
        verify(producer).sendPayment(any());
    }
    @Test
    void shouldReturnExistingPaymentWhenExternalIdExists() {

        String externalId = "123";

        Payment existing = new Payment();
        existing.setExternalId(externalId);

        when(repository.findByExternalId(externalId))
                .thenReturn(Optional.of(existing));

        Payment result = service.createPayment(
                new PaymentRequestDTO(BigDecimal.valueOf(100.00), "Teste"),
                externalId
        );

        assertEquals(existing, result);
        verify(repository, never()).save(any());
    }
    @Test
    void shouldNotCreateDuplicatePayment() {

        String externalId = "123";

        Payment existing = new Payment();
        existing.setExternalId(externalId);

        when(repository.findByExternalId(externalId))
                .thenReturn(Optional.of(existing));

        Payment result = service.createPayment(
                new PaymentRequestDTO(BigDecimal.valueOf(100.00), "Teste"),
                externalId
        );

        assertEquals(existing, result);
        verify(repository, never()).save(any());

    }
}
