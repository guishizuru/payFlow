package com.br.payFlow.controller;

import com.br.payFlow.dto.request.CustomerRequestDTO;
import com.br.payFlow.dto.response.CustomerResponseDTO;
import com.br.payFlow.entity.Customer;
import com.br.payFlow.mapper.CustomerMapper;
import com.br.payFlow.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public CustomerResponseDTO create(@RequestBody CustomerRequestDTO request) {
        Customer customer = service.creatCustomer(request);
        return CustomerMapper.toResponse(customer);
    }
}
