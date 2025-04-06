package com.keven1z.core.error;

public class TaintException extends Exception{
    TaintException(String message){
        super(message);
    }
    TaintException(String message, Throwable cause){
        super(message, cause);
    }
    TaintException(){
        super();
    }
}
