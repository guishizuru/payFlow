package com.br.payFlow.service;


import com.br.payFlow.dto.request.PaymentRequestDTO;
import com.br.payFlow.entity.Customer;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.entity.PaymentStatus;
import com.br.payFlow.messaging.producer.PaymentProducer;
import com.br.payFlow.repository.CustomerRepository;
import com.br.payFlow.repository.PaymentRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Builder
@Slf4j
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;
    private final CustomerRepository customerRepository;
    private final PaymentProducer producer;

    public Payment createPayment(PaymentRequestDTO request, String externalId) {

        if (externalId == null || externalId.isBlank()) {
            throw new IllegalArgumentException("Idempotency-Key é obrigatório");
        }

        return repository.findByExternalId(externalId)
                .orElseGet(() -> {

                    log.info("Criando pagamento externalId={}", externalId);

                    Customer customer = customerRepository.findById(request.getCustomerId())
                            .orElseThrow(() -> new RuntimeException("Customer não encontrado"));

                    Payment payment = Payment.builder()
                            .amount(request.getAmount())
                            .status(PaymentStatus.PENDING)
                            .externalId(externalId)
                            .description(request.getDescription())
                            .customerId(customer.getId())
                            .createdAt(LocalDateTime.now())
                            .build();

                    try {
                        Payment saved = repository.save(payment);
                        producer.sendPayment(saved);

                        log.info("Pagamento criado id={} status={}", saved.getId(), saved.getStatus());

                        return saved;

                    } catch (DataIntegrityViolationException e) {
                        log.warn("Concorrência detectada externalId={}", externalId);
                        return repository.findByExternalId(externalId).orElseThrow();
                    }
                });
    }
}

