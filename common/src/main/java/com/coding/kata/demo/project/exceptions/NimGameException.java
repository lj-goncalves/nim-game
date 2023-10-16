package com.coding.kata.demo.project.exceptions;

public class NimGameException extends RuntimeException{
    private static final long serialVersionUID = 9199397752991471961L;

    private Integer errorCode;

    public NimGameException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
