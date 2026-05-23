package com.vpn.vpn_backend.exceptions;

public class DataIntegrityViolationException extends RuntimeException {
    public DataIntegrityViolationException(String message){
        super(message);
    }
}
