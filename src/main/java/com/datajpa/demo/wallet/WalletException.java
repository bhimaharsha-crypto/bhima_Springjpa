package com.datajpa.demo.wallet;

// Custom or User defined exception
public class WalletException extends RuntimeException{
    public WalletException(String message){
        super(message); //custom message
    }
}
