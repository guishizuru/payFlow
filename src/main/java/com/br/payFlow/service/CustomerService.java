package com.br.payFlow.service;

import com.br.payFlow.dto.request.CustomerRequestDTO;
import com.br.payFlow.dto.response.CustomerResponseDTO;
import com.br.payFlow.entity.Customer;
import com.br.payFlow.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    public Customer creatCustomer(CustomerRequestDTO request){
        repository.findByCpf(request.cpf()).ifPresent(
                c ->{ throw new RuntimeException("CPF já cadastrado");
                }
        );
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(request.name());
        customer.setCpf(request.cpf());
        customer.setCreatedAt(LocalDateTime.now());

        return repository.save(customer);
    }
}
