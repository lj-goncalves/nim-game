package com.coding.kata.demo.project.dto;

import java.io.Serializable;

public class GameDetailsDTO implements Serializable {
    private static final long serialVersionUID = 8495855098707481671L;

    private Long gameId;

    private Integer maxMatchesPlayerCanTake;

    private Integer playerLastNim;

    private Integer cpuLastNim;

    private Integer remainingMatches;

    private String message;

    public GameDetailsDTO() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Integer getMaxMatchesPlayerCanTake() {
        return maxMatchesPlayerCanTake;
    }

    public void setMaxMatchesPlayerCanTake(Integer maxMatchesPlayerCanTake) {
        this.maxMatchesPlayerCanTake = maxMatchesPlayerCanTake;
    }

    public Integer getPlayerLastNim() {
        return playerLastNim;
    }

    public void setPlayerLastNim(Integer playerLastNim) {
        this.playerLastNim = playerLastNim;
    }

    public Integer getCpuLastNim() {
        return cpuLastNim;
    }

    public void setCpuLastNim(Integer cpuLastNim) {
        this.cpuLastNim = cpuLastNim;
    }

    public Integer getRemainingMatches() {
        return remainingMatches;
    }

    public void setRemainingMatches(Integer remainingMatches) {
        this.remainingMatches = remainingMatches;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}



