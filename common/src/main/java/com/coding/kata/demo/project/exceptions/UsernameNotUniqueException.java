package com.coding.kata.demo.project.exceptions;

import com.coding.kata.demo.project.enums.ApiErrorCode;

public class UsernameNotUniqueException extends NimGameException {
    private static final long serialVersionUID = -3137619647290307240L;

    private static final ApiErrorCode error = ApiErrorCode.USER_NOT_UNIQUE;

    public UsernameNotUniqueException() {
        super(error.getMessage(), error.getCode());
    }
}
