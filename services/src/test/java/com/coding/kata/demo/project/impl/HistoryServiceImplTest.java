package com.coding.kata.demo.project.impl;

import com.coding.kata.demo.project.dto.GameHistoryDTO;
import com.coding.kata.demo.project.dto.MoveDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.entity.GameMove;
import com.coding.kata.demo.project.entity.Player;
import com.coding.kata.demo.project.enums.StatusEnum;
import com.coding.kata.demo.project.repository.GameRepository;
import com.coding.kata.demo.project.repository.HistoryRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class HistoryServiceImplTest {

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Captor
    private ArgumentCaptor<GameMove> moveCaptor;

    private Game game;

    private Set<GameMove> moves;
    private GameMove move1;

    private GameMove move2;

    private GameMove move3;

    private Player player;

    private AutoCloseable closeable;


    @Before
    public void initMocks() {
        closeable = MockitoAnnotations.openMocks(this);

        player = new Player();
        player.setId(1);
        player.setUsername("playerName1");

        game = new Game();
        game.setId(1L);
        game.setStatus(StatusEnum.ACTIVE.name());
        game.setRemainingMatches(1);
        game.setNumberOfMatchesPlayerCanTake(2);
        game.setPlayerLastNim(2);
        game.setCpuLastNim(3);
        game.setPlayer(player);


        moves = new HashSet<>();
        move1 = new GameMove();
        move1.setId(1L);
        move1.setPlayerNim(3);
        move1.setCpuNim(1);
        move1.setRemainingMatches(9);
        moves.add(move1);

        move2 = new GameMove();
        move2.setId(2L);
        move2.setPlayerNim(1);
        move2.setCpuNim(3);
        move2.setRemainingMatches(5);
        moves.add(move2);

        move3 = new GameMove();
        move3.setId(3L);
        move3.setPlayerNim(2);
        move3.setCpuNim(2);
        move3.setRemainingMatches(1);
        moves.add(move3);

        game.setHistoryEntries(moves);

    }


    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void createHistoryEntry() {
        long id = game.getId();
        Mockito.when(gameRepository.findById(id)).thenReturn(Optional.ofNullable(game));
        historyService.createHistoryEntry(id);

        //assert that the history entry was saved
        verify(historyRepository, Mockito.times(1)).save(moveCaptor.capture());
        GameMove capturedValue = moveCaptor.getValue();
        Assert.assertEquals(game.getPlayerLastNim(), capturedValue.getPlayerNim());
        Assert.assertEquals(game.getCpuLastNim(), capturedValue.getCpuNim());
        Assert.assertEquals(game.getRemainingMatches(), capturedValue.getRemainingMatches());
        Assert.assertEquals(game.getId(), capturedValue.getGame().getId());
    }

    @Test
    public void getGameHistoryForUser() {
        String username =player.getUsername();
        List<Game> gameList = new ArrayList<>();
        gameList.add(game);

        Mockito.when(gameRepository.findByPlayerUsernameOrderByIdAsc(username)).thenReturn(gameList);

        List<GameHistoryDTO> history = historyService.getGameHistoryForUser(username);
        List<MoveDTO> moves = history.get(0).getMoves();

        //assert history information (sorted)
        Assert.assertEquals(1, history.size());
        Assert.assertEquals(3, moves.size());

        //first move
        Assert.assertEquals(move1.getId(), moves.get(0).getId().longValue());
        Assert.assertEquals(move1.getCpuNim(), moves.get(0).getCpuNim().intValue());
        Assert.assertEquals(move1.getPlayerNim(), moves.get(0).getPlayerNim().intValue());
        Assert.assertEquals(move1.getRemainingMatches(), moves.get(0).getRemainingMatches().intValue());

        //second move
        Assert.assertEquals(move2.getId(), moves.get(1).getId().longValue());
        Assert.assertEquals(move2.getCpuNim(), moves.get(1).getCpuNim().intValue());
        Assert.assertEquals(move2.getPlayerNim(), moves.get(1).getPlayerNim().intValue());
        Assert.assertEquals(move2.getRemainingMatches(), moves.get(1).getRemainingMatches().intValue());


        //third move
        Assert.assertEquals(move3.getId(), moves.get(2).getId().longValue());
        Assert.assertEquals(move3.getCpuNim(), moves.get(2).getCpuNim().intValue());
        Assert.assertEquals(move3.getPlayerNim(), moves.get(2).getPlayerNim().intValue());
        Assert.assertEquals(move3.getRemainingMatches(), moves.get(2).getRemainingMatches().intValue());

    }
}