package com.revature.exceptions;

public class InvalidOrderException extends RuntimeException{
    public InvalidOrderException(String msg){
        super(msg);
    }
}
