package com.coding.kata.demo.project.impl;

import com.coding.kata.demo.project.HistoryService;
import com.coding.kata.demo.project.dto.GameHistoryDTO;
import com.coding.kata.demo.project.dto.MoveDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.entity.GameMove;
import com.coding.kata.demo.project.exceptions.ActiveGameNotFoundException;
import com.coding.kata.demo.project.mapper.GameMoveMapper;
import com.coding.kata.demo.project.repository.GameRepository;
import com.coding.kata.demo.project.repository.HistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class HistoryServiceImpl implements HistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoryServiceImpl.class);
    @Autowired
    GameRepository gameRepository;

    @Autowired
    HistoryRepository historyRepository;


    @Transactional
    public void createHistoryEntry(long gameId) {
        //get the game
        Game game = gameRepository.findById(gameId).orElse(null);

        if(game == null) {
            LOG.error("@createHistoryEntry: failed to fetch game with id '{}'", gameId);
            throw new ActiveGameNotFoundException();
        }

        GameMove gameMove = new GameMove();
        gameMove.setGame(game);
        gameMove.setRemainingMatches(game.getRemainingMatches());
        gameMove.setCpuNim(game.getCpuLastNim());
        gameMove.setPlayerNim(game.getPlayerLastNim());

        historyRepository.save(gameMove);
    }

    @Override
    public List<GameHistoryDTO> getGameHistoryForUser(String username) {

        List<GameHistoryDTO> gameHistory = new ArrayList<>();

        //get all the user's games sorted by ID
        List<Game> games = gameRepository.findByPlayerUsernameOrderByIdAsc(username);
        for(Game game : games) {
            GameHistoryDTO gameHistoryEntry = new GameHistoryDTO();
            gameHistoryEntry.setGameId(game.getId());
            gameHistoryEntry.setWinner(game.getWinner());

            //get the moves for this game
            List<MoveDTO> moves = GameMoveMapper.convertGameMoveListIntoMoveDTOList(new ArrayList<>(game.getHistoryEntries()));
            //sort moves by ID
            moves.sort(Comparator.comparing(MoveDTO::getId));
            gameHistoryEntry.setMoves(moves);

            gameHistory.add(gameHistoryEntry);
        }

        return gameHistory;
    }


}
