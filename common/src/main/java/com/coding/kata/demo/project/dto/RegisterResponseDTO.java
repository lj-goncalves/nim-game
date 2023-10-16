package com.coding.kata.demo.project.dto;

import java.io.Serializable;

public class RegisterResponseDTO implements Serializable {
    private static final long serialVersionUID = -3953295489697877560L;

    private long playerId;

    public RegisterResponseDTO() {
    }

    public RegisterResponseDTO(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
