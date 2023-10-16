package com.coding.kata.demo.project.impl;

import com.coding.kata.demo.project.GameService;
import com.coding.kata.demo.project.dto.GameDetailsDTO;
import com.coding.kata.demo.project.dto.PlayRequestDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.entity.Player;
import com.coding.kata.demo.project.enums.GameAgentType;
import com.coding.kata.demo.project.enums.StatusEnum;
import com.coding.kata.demo.project.exceptions.ActiveGameNotFoundException;
import com.coding.kata.demo.project.exceptions.InvalidMoveException;
import com.coding.kata.demo.project.mapper.GameInfoMapper;
import com.coding.kata.demo.project.repository.GameRepository;
import com.coding.kata.demo.project.repository.PlayerRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class GameServiceImpl implements GameService {
    private static final Logger LOG = LoggerFactory.getLogger(GameServiceImpl.class);

    @Value("${number.of.matches.at.game.start:13}")
    private Integer numberOfMatchesAtStart;

    @Value("${max.number.of.matches.player.can.take:3}")
    private Integer numberOfMatchesPlayerCanTake;

    @Value("${is.cpu.smart:true}")
    private Boolean isCpuSmart;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    //public for test purposes
    public List<Integer> magicNumbers;

    @PostConstruct
    public void init() {
        //This magic number list contains the numbers that are favorable for the cpu victory
        magicNumbers = new ArrayList<>();

        //first magic number, when cpu leaves 1 match for the player it ensures a cpu win
        int nextMagicNumber = 1;

        //calculate next magic numbers
        while(nextMagicNumber <= numberOfMatchesAtStart) {
            magicNumbers.add(nextMagicNumber);

            //max possible move + 1
            //this ensures that if we leave a magic number for the player he can never reach the next magic number in his next play
            //but the cpu can
            int magicNumberIncrement = numberOfMatchesPlayerCanTake + 1;

            nextMagicNumber += magicNumberIncrement;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public GameDetailsDTO getGameInformationForUsername(String username)  {
        //get player's active game
        Game game = gameRepository.findByPlayerUsernameAndStatus(username, StatusEnum.ACTIVE.name());

        if(game == null) {
            LOG.error("@getGameInformationForUsername: player '{}' does not have an active game", username);
            throw new ActiveGameNotFoundException();
        }

        return GameInfoMapper.convertGameIntoGameDetailsDTO(game);
    }

    @Override
    @Transactional
    public void createNewGameForUser(Player user) {
        String username = user.getUsername();
        LOG.info("@createNewGameForUser: Creating a new game for Player '{}'", username);

        //make sure to finalize the user's previous game
        Game game = gameRepository.findByPlayerUsernameAndStatus(username, StatusEnum.ACTIVE.name());
        if(game != null) {
            game.setStatus(StatusEnum.FINALISED.name());
        }

        gameRepository.save(
                new Game(
                        numberOfMatchesAtStart,
                        0,
                        0,
                        StatusEnum.ACTIVE.name(),
                        null,
                        new Date(),
                        user,
                        numberOfMatchesPlayerCanTake
                )
        );
    }

    @Override
    @Transactional
    public GameDetailsDTO playNimGameTurn(PlayRequestDTO playRequest) {
        String username = playRequest.getUsername();
        int playerNim = playRequest.getNim();

        //get player's active game
        Game game = gameRepository.findByPlayerUsernameAndStatus(username, StatusEnum.ACTIVE.name());

        if(game == null) {
            LOG.error("@playNimGameTurn: player '{}' does not have an active game", username);
            throw new ActiveGameNotFoundException();
        }
        LOG.info("@playNimGameTurn: there are '{}' remaining matches", game.getRemainingMatches());

        //validate player move
        validatePlayerMove(playerNim, game.getRemainingMatches(), username);

        //calculate player move
        int currentHeap = game.getRemainingMatches() - playerNim;
        updateGameAfterMove(game, playerNim, currentHeap, GameAgentType.PLAYER);
        LOG.info("@playNimGameTurn: player '{}' took '{}' matches, there are '{}' remaining", username, playerNim, currentHeap);

        //validate if CPU won
        if(currentHeap == 0) {
            return handleGameFinish(game, GameAgentType.CPU);
        }

        //calculate cpu move
        int cpuNim = calculateCpuMove(currentHeap);
        currentHeap -= cpuNim;
        updateGameAfterMove(game, cpuNim, currentHeap, GameAgentType.CPU);
        LOG.info("@playNimGameTurn: CPU took took '{}' matches, there are '{}' remaining", cpuNim, currentHeap);

        //validate if Player won
        if(currentHeap == 0) {
            return handleGameFinish(game, GameAgentType.PLAYER);
        }

        return GameInfoMapper.convertGameIntoGameDetailsDTO(game);
    }

    private void validatePlayerMove (int playerNim, int currentHeap, String username) {
        if(playerNim > currentHeap || playerNim > numberOfMatchesPlayerCanTake) {
            LOG.error("@validatePlayerMove: player '{}' took too many matches in this turn", username);
            throw new InvalidMoveException();
        }
    }

    private void updateGameAfterMove(Game game, int nim, int currentHeap, GameAgentType agent) {
        Date now = new Date();
        game.setLastModifiedTs(now);
        game.setRemainingMatches(currentHeap);

        if(GameAgentType.PLAYER.equals(agent)) {
            //update player's last move
            game.setPlayerLastNim(nim);

        } else {
            //update cpu's last move
            game.setCpuLastNim(nim);
        }
    }

    private GameDetailsDTO handleGameFinish(Game game, GameAgentType winner) {
        //when CPU wins it does not draw the last match
        if(GameAgentType.CPU.equals(winner)) {
            game.setCpuLastNim(0);
        }
        game.setWinner(winner.name());
        LOG.info("@handleGameFinish: game has finished '{}' has won!", winner.name());

        createNewGameForUser(game.getPlayer());
        return GameInfoMapper.convertGameIntoGameDetailsDTO(game);
    }

    private int calculateCpuMove(int currentHeap) {
        int cpuMaxMove = Math.min(currentHeap, numberOfMatchesPlayerCanTake);

        //the objective of a smart play is to always leave a "magic number" of matches for the player
        //at the end of the game we should leave the player with 1 match, making sure the cpu wins the game
        if(isCpuSmart) {
            //find the next reachable magic number
            for(int i = 0; i < magicNumbers.size(); i++) {
                int currentMagicNumber = magicNumbers.get(i);

                if(currentHeap == currentMagicNumber) {
                    //if the player left a magic number for the cpu then we can't reach the next magic number
                    //the increments between magic numbers are made from (max play + 1), we can only make a max play
                    break;
                }

                if(currentHeap < currentMagicNumber) {
                    //next reachable magic number found
                    int targetMagicNumber = magicNumbers.get(i -1);
                    return currentHeap - targetMagicNumber;
                }
            }
        }

        //random value between 1 and cpuMaxMove
        return new Random().nextInt(cpuMaxMove) + 1;
    }

    private boolean validateCpuMove(int cpuNim, int cpuMaxMove) {
        if(cpuNim > 0 && cpuNim <= cpuMaxMove) {
            return true;
        }
        return false;
    }


}
