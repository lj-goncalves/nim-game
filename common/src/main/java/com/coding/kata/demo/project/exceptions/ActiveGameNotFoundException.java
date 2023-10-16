package com.coding.kata.demo.project.exceptions;

import com.coding.kata.demo.project.enums.ApiErrorCode;

public class ActiveGameNotFoundException extends NimGameException {
    private static final long serialVersionUID = 2867132766053754361L;

    private static final ApiErrorCode error = ApiErrorCode.GAME_NOT_FOUND;

    public ActiveGameNotFoundException() {
        super(error.getMessage(), error.getCode());
    }
}
