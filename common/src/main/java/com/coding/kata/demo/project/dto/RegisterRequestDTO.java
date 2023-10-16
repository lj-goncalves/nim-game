package com.coding.kata.demo.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class RegisterRequestDTO implements Serializable {
    private static final long serialVersionUID = -3953295489697877560L;

    @NotBlank(message = "please provide a username")
    @Size(max = 16, message = "please provide a username that shorter than 16 characters")
    private String username;

    public RegisterRequestDTO(String username) {
        this.username = username;
    }

    public RegisterRequestDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
