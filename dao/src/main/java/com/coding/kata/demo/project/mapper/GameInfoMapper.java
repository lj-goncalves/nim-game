package com.coding.kata.demo.project.mapper;

import com.coding.kata.demo.project.dto.GameDetailsDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.enums.GameAgentType;

public class GameInfoMapper {

    public static GameDetailsDTO convertGameIntoGameDetailsDTO(Game game) {
        GameDetailsDTO gameDTO = new GameDetailsDTO();
        gameDTO.setCpuLastNim(game.getCpuLastNim());
        gameDTO.setPlayerLastNim(game.getPlayerLastNim());
        gameDTO.setRemainingMatches(game.getRemainingMatches());
        gameDTO.setGameId(game.getId());
        gameDTO.setMaxMatchesPlayerCanTake(game.getNumberOfMatchesPlayerCanTake());

        if(GameAgentType.CPU.name().equals(game.getWinner())) {
            gameDTO.setMessage("CPU has won the game!");
        } else if(GameAgentType.PLAYER.name().equals(game.getWinner())) {
            gameDTO.setMessage("you have won the game!");
        }

        return gameDTO;
    }
}
