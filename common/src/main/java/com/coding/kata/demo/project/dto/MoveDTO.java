package com.coding.kata.demo.project.dto;

import java.io.Serializable;

public class MoveDTO implements Serializable  {
    private static final long serialVersionUID = -6445968907921166544L;

    private Long id;

    private Integer playerNim;

    private Integer cpuNim;

    private Integer remainingMatches;

    public MoveDTO() {
    }

    public MoveDTO(Integer playerNim, Integer cpuNim, Integer remainingMatches) {
        this.remainingMatches = remainingMatches;
        this.playerNim = playerNim;
        this.cpuNim = cpuNim;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPlayerNim() {
        return playerNim;
    }

    public void setPlayerNim(Integer playerNim) {
        this.playerNim = playerNim;
    }

    public Integer getCpuNim() {
        return cpuNim;
    }

    public void setCpuNim(Integer cpuNim) {
        this.cpuNim = cpuNim;
    }

    public Integer getRemainingMatches() {
        return remainingMatches;
    }

    public void setRemainingMatches(Integer remainingMatches) {
        this.remainingMatches = remainingMatches;
    }
}
