package com.br.payFlow.dto.response;

import java.util.UUID;

public record CustomerResponseDTO(
        UUID id,
        String name,
        String cpf
) {}
