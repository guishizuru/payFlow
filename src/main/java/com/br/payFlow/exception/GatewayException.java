package com.br.payFlow.exception;

public class GatewayException  extends RuntimeException{
    public GatewayException(String message, Throwable cause){
        super(message, cause);
    }
}
