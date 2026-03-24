package com.br.payFlow.gateway;

import com.br.payFlow.entity.Payment;

public interface PaymentGateway {

    boolean process(Payment payment);
}
