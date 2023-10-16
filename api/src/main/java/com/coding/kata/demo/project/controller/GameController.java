package com.coding.kata.demo.project.controller;


import com.coding.kata.demo.project.GameService;
import com.coding.kata.demo.project.HistoryService;
import com.coding.kata.demo.project.PlayerService;
import com.coding.kata.demo.project.dto.GameHistoryDTO;
import com.coding.kata.demo.project.dto.RegisterRequestDTO;
import com.coding.kata.demo.project.dto.GameDetailsDTO;
import com.coding.kata.demo.project.dto.PlayRequestDTO;
import com.coding.kata.demo.project.dto.RegisterResponseDTO;
import com.coding.kata.demo.project.exceptions.UsernameNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/game")
public class GameController {
    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private HistoryService historyService;

    @Tag(name = "register", description = "Create a new player with an active game")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
        LOG.info("Player '{}' registering to play the Nim game", request.getUsername());

        Long userId = playerService.registerNewUser(request);

        return new ResponseEntity<>(new RegisterResponseDTO(userId), HttpStatus.OK);
    }

    @Tag(name = "info", description = "Get information about the player's current game")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public ResponseEntity<GameDetailsDTO> info(@RequestParam String username) {
        LOG.info("Player '{}' requesting information about the current game", username);

        if(!playerService.existsPlayerWithUsername(username)) {
            LOG.error("Failed to fetch game information, username '{}' not found", username);
            throw new UsernameNotFoundException();
        }
        return new ResponseEntity<>(gameService.getGameInformationForUsername(username), HttpStatus.OK);
    }

    @Tag(name = "play", description = "Play Nim game")
    @RequestMapping(value = "/play", method = RequestMethod.POST)
    public ResponseEntity<GameDetailsDTO> play(@RequestBody @Valid PlayRequestDTO request) {
        LOG.info("Player '{}' playing a round of the Nim game", request.getUsername());
        if(!playerService.existsPlayerWithUsername(request.getUsername())) {
            LOG.error("Failed to fetch game information, username  '{}' not found", request.getUsername());
            throw new UsernameNotFoundException();
        }
        //play a round
        GameDetailsDTO gameDetails = gameService.playNimGameTurn(request);

        //write in history
        historyService.createHistoryEntry(gameDetails.getGameId());

        return new ResponseEntity<>(gameDetails, HttpStatus.OK);
    }


    @Tag(name = "history", description = "Get the full history of the player's games")
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<List<GameHistoryDTO>> history(@RequestParam String username) {
        LOG.info("Player '{}' requesting game history", username);

        if(!playerService.existsPlayerWithUsername(username)) {
            LOG.error("Failed to fetch game history, username  '{}' not found", username);
            throw new UsernameNotFoundException();
        }

        return new ResponseEntity<>(historyService.getGameHistoryForUser(username), HttpStatus.OK);
    }


}
