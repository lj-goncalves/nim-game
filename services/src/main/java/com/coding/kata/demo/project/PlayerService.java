package com.coding.kata.demo.project;

import com.coding.kata.demo.project.dto.RegisterRequestDTO;
import com.coding.kata.demo.project.exceptions.UsernameNotUniqueException;

public interface PlayerService {

    Long registerNewUser(RegisterRequestDTO requestDTO);

    boolean existsPlayerWithUsername(String username);
}
