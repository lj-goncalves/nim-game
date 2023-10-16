package com.coding.kata.demo.project.dto;

import java.io.Serializable;
import java.util.List;

public class GameHistoryDTO implements Serializable {
    private static final long serialVersionUID = -3953295489697877560L;

    private Long gameId;

    private String winner;

    private List<MoveDTO> moves;

    public GameHistoryDTO() {
    }

    public GameHistoryDTO(Long gameId, String winner, List<MoveDTO> moves) {
        this.gameId = gameId;
        this.winner = winner;
        this.moves = moves;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public List<MoveDTO> getMoves() {
        return moves;
    }

    public void setMoves(List<MoveDTO> moves) {
        this.moves = moves;
    }
}
