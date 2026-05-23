package com.vpn.vpn_backend.exceptions;

public class UnAuthorizedException extends RuntimeException{
    public UnAuthorizedException(String message){
        super(message);
    }
}
