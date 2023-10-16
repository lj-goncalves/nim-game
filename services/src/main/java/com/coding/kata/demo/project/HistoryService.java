package com.coding.kata.demo.project;

import com.coding.kata.demo.project.dto.GameHistoryDTO;

import java.util.List;

public interface HistoryService {

    void createHistoryEntry(long gameId);

    List<GameHistoryDTO> getGameHistoryForUser(String username);
}
