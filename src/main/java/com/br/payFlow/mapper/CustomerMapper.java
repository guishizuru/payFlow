package com.br.payFlow.mapper;

import com.br.payFlow.dto.response.CustomerResponseDTO;
import com.br.payFlow.entity.Customer;

public class CustomerMapper {
    public static CustomerResponseDTO toResponse(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getCpf()
        );
    }
}
