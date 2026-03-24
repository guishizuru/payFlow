package com.br.payFlow.mapper;

import com.br.payFlow.dto.response.PaymentResponseDTO;
import com.br.payFlow.entity.Payment;

public class PaymentMapper {
    public static PaymentResponseDTO toResponse(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .status(payment.getStatus().name())
                .build();
    }
}