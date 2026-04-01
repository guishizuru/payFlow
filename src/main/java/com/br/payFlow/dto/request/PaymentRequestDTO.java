package com.br.payFlow.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentRequestDTO {

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;

    private UUID customerId;
}
