package com.br.payFlow.service;


import com.br.payFlow.dto.request.PaymentRequestDTO;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.entity.PaymentStatus;
import com.br.payFlow.messaging.producer.PaymentProducer;
import com.br.payFlow.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentProducer producer;

    public Payment createPayment(PaymentRequestDTO request, String externalId) {

        return repository.findByExternalId(externalId)
                .orElseGet(() -> {

                    Payment payment = Payment.builder()
                            .id(UUID.randomUUID())
                            .amount(request.getAmount())
                            .status(PaymentStatus.PENDING)
                            .externalId(externalId)
                            .description(request.getDescription())
                            .createdAt(LocalDateTime.now())
                            .build();

                    try {
                        Payment saved = repository.save(payment);
                        producer.sendPayment(saved);

                        return saved;
                    } catch (DataIntegrityViolationException e) {
                        return repository.findByExternalId(externalId).orElseThrow();
                    }
                });
    }
}
