package com.coding.kata.demo.project.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class PlayRequestDTO implements Serializable {
    private static final long serialVersionUID = 6163808615173842814L;
    @NotBlank(message = "please provide a username")
    private String username;

    @Min(value = 1, message = "choose a value between 1 and 3")
    @Max(value = 3, message = "choose a value between 1 and 3")
    private Integer nim;

    public PlayRequestDTO(String username, Integer nim) {
        this.username = username;
        this.nim = nim;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getNim() {
        return nim;
    }

    public void setNim(Integer nim) {
        this.nim = nim;
    }
}
