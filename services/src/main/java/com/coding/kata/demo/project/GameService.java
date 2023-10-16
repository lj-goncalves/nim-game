package com.coding.kata.demo.project;

import com.coding.kata.demo.project.dto.GameDetailsDTO;
import com.coding.kata.demo.project.dto.PlayRequestDTO;
import com.coding.kata.demo.project.entity.Player;

public interface GameService {

    GameDetailsDTO getGameInformationForUsername(String username);

    void createNewGameForUser(Player user);

    GameDetailsDTO playNimGameTurn(PlayRequestDTO playRequest);

}
