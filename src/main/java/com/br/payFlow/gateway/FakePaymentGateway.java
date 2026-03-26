package com.br.payFlow.gateway;

import com.br.payFlow.entity.Payment;
import com.br.payFlow.exception.GatewayException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
public class FakePaymentGateway implements PaymentGateway {

    private final Random random;

    public FakePaymentGateway() {
        this.random = new Random();
    }
    public FakePaymentGateway(Random random) {
        this.random = random;
    }

    @Override
    @CircuitBreaker(name = "paymentGateway", fallbackMethod = "fallback")
    public boolean process(Payment payment) {

        int chance = random.nextInt(100);

        if (chance < 70) {
            return true;
        }
        if (chance < 90) {
            return false;
        }

        throw new RuntimeException("Gateway fora do ar");
    }
    public boolean fallback(Payment payment, Throwable ex) {
        log.error("Circuit breaker ativado para pagamento id={}", payment.getId(), ex);
        throw new GatewayException("Fallback acionado", ex);
    }
}
