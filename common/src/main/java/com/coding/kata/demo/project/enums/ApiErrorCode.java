package com.coding.kata.demo.project.enums;

public enum ApiErrorCode {
    OK("ok", 0),
    UNEXPECTED_ERROR("unexpected error occurred", 1),
    INVALID_PARAMETER("invalid parameter", 2),
    USER_NOT_FOUND("username not found", 3),
    USER_NOT_UNIQUE("username is not unique", 4),
    GAME_NOT_FOUND("game not found", 5),
    INVALID_MOVE("that move is not allowed", 6)
    ;

    private String message;
    private int code;


    ApiErrorCode(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
