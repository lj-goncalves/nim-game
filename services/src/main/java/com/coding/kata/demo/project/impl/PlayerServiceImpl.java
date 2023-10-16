package com.coding.kata.demo.project.impl;

import com.coding.kata.demo.project.GameService;
import com.coding.kata.demo.project.PlayerService;
import com.coding.kata.demo.project.dto.RegisterRequestDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.entity.Player;
import com.coding.kata.demo.project.enums.StatusEnum;
import com.coding.kata.demo.project.exceptions.UsernameNotUniqueException;
import com.coding.kata.demo.project.repository.GameRepository;
import com.coding.kata.demo.project.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final Logger LOG = LoggerFactory.getLogger(PlayerServiceImpl.class);

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameService gameService;

    @Override
    @Transactional
    public Long registerNewUser(RegisterRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        Player user = playerRepository.findByUsername(username);

        if(user != null) {
            LOG.error("@registerNewUser: username '{}' is not unique", username);
            throw new UsernameNotUniqueException();
        }
        //register a new player
        user = playerRepository.save(new Player(username));

        //create a new game for the player
        gameService.createNewGameForUser(user);
        return user.getId();
    }

    @Override
    public boolean existsPlayerWithUsername(String username) {
        return playerRepository.existsPlayerByUsername(username);
    }
}
