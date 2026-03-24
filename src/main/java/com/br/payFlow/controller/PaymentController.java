package com.br.payFlow.controller;

import com.br.payFlow.dto.request.PaymentRequestDTO;
import com.br.payFlow.dto.response.PaymentResponseDTO;
import com.br.payFlow.entity.Payment;
import com.br.payFlow.mapper.PaymentMapper;
import com.br.payFlow.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponseDTO createPayment(
            @RequestBody @Valid PaymentRequestDTO requestDTO,
            @RequestHeader("Idempotency-Key") String externalId){

        Payment payment = paymentService.createPayment(requestDTO, externalId);
        return PaymentMapper.toResponse(payment);

    }
}
