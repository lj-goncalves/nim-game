package com.coding.kata.demo.project.mapper;

import com.coding.kata.demo.project.dto.GameDetailsDTO;
import com.coding.kata.demo.project.dto.MoveDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.entity.GameMove;
import com.coding.kata.demo.project.enums.GameAgentType;

import java.util.ArrayList;
import java.util.List;

public class GameMoveMapper {

    public static MoveDTO convertGameMoveIntoMoveDTO(GameMove move) {
        MoveDTO moveDTO = new MoveDTO();
        moveDTO.setId(move.getId());
        moveDTO.setCpuNim(move.getCpuNim());
        moveDTO.setPlayerNim(move.getPlayerNim());
        moveDTO.setRemainingMatches(move.getRemainingMatches());

        return moveDTO;
    }

    public static List<MoveDTO> convertGameMoveListIntoMoveDTOList(List<GameMove> moves) {

        List<MoveDTO> moveDTOList = new ArrayList<>();

        for(GameMove gameMove : moves) {
            moveDTOList.add(convertGameMoveIntoMoveDTO(gameMove));
        }

        return moveDTOList;
    }

}
