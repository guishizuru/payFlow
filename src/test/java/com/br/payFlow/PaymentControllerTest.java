package com.br.payFlow;

import com.br.payFlow.dto.request.PaymentRequestDTO;
import com.br.payFlow.dto.response.PaymentResponseDTO;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.entity.PaymentStatus;
import com.br.payFlow.mapper.PaymentMapper;
import com.br.payFlow.messaging.producer.PaymentProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
/*@ActiveProfiles("test")*/
class PaymentControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

 /*   @MockBean
    private PaymentProducer producer;*/
    @Test
    void shouldCreatePayment() {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Idempotency-Key", "123");

        PaymentRequestDTO request =
                new PaymentRequestDTO(BigDecimal.valueOf(100.00), "Teste");

        HttpEntity<PaymentRequestDTO> entity =
                new HttpEntity<>(request, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity("/payments", entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    @Test
    void shouldMapPayment() {

        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(100));
        payment.setStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());

        PaymentResponseDTO response = PaymentMapper.toResponse(payment);

        assertNotNull(response);
    }
}
