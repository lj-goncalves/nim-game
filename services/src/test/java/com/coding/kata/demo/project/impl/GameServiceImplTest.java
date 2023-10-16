package com.coding.kata.demo.project.impl;

import com.coding.kata.demo.project.dto.GameDetailsDTO;
import com.coding.kata.demo.project.dto.PlayRequestDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.entity.Player;
import com.coding.kata.demo.project.enums.GameAgentType;
import com.coding.kata.demo.project.enums.StatusEnum;
import com.coding.kata.demo.project.exceptions.ActiveGameNotFoundException;
import com.coding.kata.demo.project.exceptions.InvalidMoveException;
import com.coding.kata.demo.project.repository.GameRepository;
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
import org.springframework.test.util.ReflectionTestUtils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class GameServiceImplTest {
    private static final int DEFAULT_MATCHES_AT_START = 13;
    private static final int DEFAULT_MATCHES_CAN_TAKE = 3;
    private static final int CUSTOM_MATCHES_AT_START = 10;
    private static final int CUSTOM_MATCHES_CAN_TAKE = 2;

    @InjectMocks
    private GameServiceImpl gameService;

    @Mock
    private GameRepository gameRepository;

    private AutoCloseable closeable;

    private Game game;

    private Player player;

    @Captor
    ArgumentCaptor<Game> gameCaptor;

    @Before
    public void initMocks() {

        closeable = MockitoAnnotations.openMocks(this);

        //test the default configuration of the nim game
        ReflectionTestUtils.setField(gameService, "numberOfMatchesAtStart", DEFAULT_MATCHES_AT_START);
        ReflectionTestUtils.setField(gameService, "numberOfMatchesPlayerCanTake", DEFAULT_MATCHES_CAN_TAKE);
        ReflectionTestUtils.setField(gameService, "isCpuSmart", true);

        //init a magic number list of { 1, 5, 9, 13 }
        gameService.init();

        player = new Player();
        player.setId(1L);
        player.setUsername("playerName1");

        game = new Game();
        game.setId(1L);
        game.setStatus(StatusEnum.ACTIVE.name());
        game.setRemainingMatches(DEFAULT_MATCHES_AT_START);
        game.setNumberOfMatchesPlayerCanTake(DEFAULT_MATCHES_CAN_TAKE);
        game.setPlayerLastNim(0);
        game.setCpuLastNim(0);
        game.setPlayer(player);
    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void initMagicNumberDefaultConfigTest() {
        //Assert that the magic number list is { 1, 5, 9, 13 }
        Assert.assertEquals(1, gameService.magicNumbers.get(0).intValue());
        Assert.assertEquals(5, gameService.magicNumbers.get(1).intValue());
        Assert.assertEquals(9, gameService.magicNumbers.get(2).intValue());
        Assert.assertEquals(13, gameService.magicNumbers.get(3).intValue());

        Assert.assertEquals(4, gameService.magicNumbers.size());
    }

    @Test
    public void initMagicNumberCustomConfigTest() {
        ReflectionTestUtils.setField(gameService, "numberOfMatchesAtStart", CUSTOM_MATCHES_AT_START);
        ReflectionTestUtils.setField(gameService, "numberOfMatchesPlayerCanTake", CUSTOM_MATCHES_CAN_TAKE);

        gameService.init();
        //Assert that the magic number list is { 1, 4, 7 ,10 }
        Assert.assertEquals(1, gameService.magicNumbers.get(0).intValue());
        Assert.assertEquals(4, gameService.magicNumbers.get(1).intValue());
        Assert.assertEquals(7, gameService.magicNumbers.get(2).intValue());
        Assert.assertEquals(10, gameService.magicNumbers.get(3).intValue());

        Assert.assertEquals(4, gameService.magicNumbers.size());
    }

    @Test
    public void getGameInformationForUsernameTest() {
        String username = "playerName1";
        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(username,StatusEnum.ACTIVE.name())).thenReturn(game);
        GameDetailsDTO gameDetails = gameService.getGameInformationForUsername(username);

        Assert.assertEquals(game.getId(), gameDetails.getGameId().longValue());
        Assert.assertEquals(game.getNumberOfMatchesPlayerCanTake(), gameDetails.getMaxMatchesPlayerCanTake().intValue());
        Assert.assertEquals(game.getPlayerLastNim(), gameDetails.getPlayerLastNim().intValue());
        Assert.assertEquals(game.getCpuLastNim(), gameDetails.getCpuLastNim().intValue());
        Assert.assertEquals(game.getRemainingMatches(), gameDetails.getRemainingMatches().intValue());

    }

    @Test(expected = ActiveGameNotFoundException.class)
    public void getGameInformationForUsernameGameNotFoundTest() {
        String username = "playerName2";
        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(username,StatusEnum.ACTIVE.name())).thenReturn(null);
        gameService.getGameInformationForUsername(username);
    }

    @Test
    public void createNewGameForUserTest() {
        String username = player.getUsername();

        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(username, StatusEnum.ACTIVE.name())).thenReturn(game);
        gameService.createNewGameForUser(player);

        // Assert that the game is now FINALISED
        assertEquals(StatusEnum.FINALISED.name(), game.getStatus());

        //assert that a new game was created for the user
        verify(gameRepository, Mockito.times(1)).save(gameCaptor.capture());
        assertNewGameProperties(gameCaptor.getValue());
    }


    @Test
    public void playNimGameTurnCpuVictoryTest() {
        //----- 1 remaining matches, player takes last -----
        game.setRemainingMatches(1);
        PlayRequestDTO playRequest = new PlayRequestDTO(player.getUsername(), 1);

        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(game);
        gameService.playNimGameTurn(playRequest);

        //assert that the game is won by the cpu
        assertFinishedGameProperties(game, GameAgentType.CPU.name(), playRequest.getNim(), 0);

        //assert that a new game was created for the user
        verify(gameRepository, Mockito.times(1)).save(gameCaptor.capture());
        assertNewGameProperties(gameCaptor.getValue());
    }

    @Test
    public void playNimGameTurnPlayerVictoryTest() {
        //----- 2 remaining matches, player takes 1 and cpu takes last -----
        game.setRemainingMatches(2);
        PlayRequestDTO playRequest = new PlayRequestDTO(player.getUsername(), 1);

        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(game);
        gameService.playNimGameTurn(playRequest);

        //assert that the game is won by the cpu
        assertFinishedGameProperties(game, GameAgentType.PLAYER.name(), playRequest.getNim(), 1);

        //assert that a new game was created for the user
        verify(gameRepository, Mockito.times(1)).save(gameCaptor.capture());
        assertNewGameProperties(gameCaptor.getValue());
    }

    @Test
    public void playNimGameDefaultConfigTest() {
        //----- 13 remaining matches, player takes 1 and cpu takes 3 -----
        game.setRemainingMatches(13);
        PlayRequestDTO playRequest = new PlayRequestDTO(player.getUsername(), 1);
        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(game);

        gameService.playNimGameTurn(playRequest);

        //assert that there are 9 matches left
        Assert.assertEquals(9, game.getRemainingMatches());
        Assert.assertEquals(3, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());


        //----- 9 remaining matches, player takes 3 and cpu takes 1 -----
        playRequest.setNim(3);

        gameService.playNimGameTurn(playRequest);

        //assert that there are 5 matches left
        Assert.assertEquals(5, game.getRemainingMatches());
        Assert.assertEquals(1, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());


        //----- 5 remaining matches, player takes 2 and cpu takes 2 -----
        playRequest.setNim(2);

        gameService.playNimGameTurn(playRequest);

        //assert that there is 1 match left
        Assert.assertEquals(1, game.getRemainingMatches());
        Assert.assertEquals(2, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());


        //----- 5 remaining matches, player takes 1 and cpu takes 3 -----
        game.setRemainingMatches(5);
        playRequest.setNim(1);

        gameService.playNimGameTurn(playRequest);

        //assert that there is 1 match left
        Assert.assertEquals(1, game.getRemainingMatches());
        Assert.assertEquals(3, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());


        //----- 5 remaining matches, player takes 3 and cpu takes 1 -----
        game.setRemainingMatches(5);
        playRequest.setNim(3);

        gameService.playNimGameTurn(playRequest);

        //assert that there is 1 match left
        Assert.assertEquals(1, game.getRemainingMatches());
        Assert.assertEquals(1, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());
    }

    @Test
    public void playNimGameCustomConfigTest() {
        ReflectionTestUtils.setField(gameService, "numberOfMatchesAtStart", CUSTOM_MATCHES_AT_START);
        ReflectionTestUtils.setField(gameService, "numberOfMatchesPlayerCanTake", CUSTOM_MATCHES_CAN_TAKE);

        //init a magic number list of { 1, 4, 7 ,10 }
        gameService.init();

        //----- 10 remaining matches, player takes 1 and cpu takes 2 -----
        game.setRemainingMatches(10);
        PlayRequestDTO playRequest = new PlayRequestDTO(player.getUsername(), 1);
        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(game);

        gameService.playNimGameTurn(playRequest);

        //assert that there are 7 matches left
        Assert.assertEquals(7, game.getRemainingMatches());
        Assert.assertEquals(2, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());


        //----- 7 remaining matches, player takes 2 and cpu takes 1 -----
        playRequest.setNim(2);
        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(game);

        gameService.playNimGameTurn(playRequest);

        //assert that there are 4 matches left
        Assert.assertEquals(4, game.getRemainingMatches());
        Assert.assertEquals(1, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());


        //----- 4 remaining matches, player takes 2 and cpu takes 1 -----
        playRequest.setNim(2);
        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(game);

        gameService.playNimGameTurn(playRequest);

        //assert that there are 4 matches left
        Assert.assertEquals(1, game.getRemainingMatches());
        Assert.assertEquals(1, game.getCpuLastNim());
        Assert.assertEquals(playRequest.getNim().intValue(), game.getPlayerLastNim());
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());
    }

    @Test(expected = InvalidMoveException.class)
    public void playNimGameTurnInvalidMoveTest() {
        PlayRequestDTO playRequest = new PlayRequestDTO(player.getUsername(), 5);

        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(game);
        gameService.playNimGameTurn(playRequest);
    }

    @Test(expected = ActiveGameNotFoundException.class)
    public void playNimGameTurnGameNotFoundTest() {
        PlayRequestDTO playRequest = new PlayRequestDTO("playerName2", 1);
        Mockito.when(gameRepository.findByPlayerUsernameAndStatus(playRequest.getUsername() ,StatusEnum.ACTIVE.name())).thenReturn(null);
        gameService.playNimGameTurn(playRequest);
    }

    private void assertFinishedGameProperties(Game game, String winner, int playerNim, int cpuNim) {
        Assert.assertEquals(winner, game.getWinner());
        Assert.assertEquals(StatusEnum.FINALISED.name(), game.getStatus());
        Assert.assertEquals(0, game.getRemainingMatches());

        Assert.assertEquals(cpuNim, game.getCpuLastNim());
        Assert.assertEquals(playerNim, game.getPlayerLastNim());
    }

    private void assertNewGameProperties(Game game) {
        Assert.assertEquals(StatusEnum.ACTIVE.name(), game.getStatus());
        Assert.assertNull(game.getWinner());
        Assert.assertEquals(DEFAULT_MATCHES_AT_START, game.getRemainingMatches());
        Assert.assertEquals(DEFAULT_MATCHES_CAN_TAKE, game.getNumberOfMatchesPlayerCanTake());
        Assert.assertEquals(0, game.getCpuLastNim());
        Assert.assertEquals(0, game.getPlayerLastNim());
        Assert.assertEquals(player, game.getPlayer());
    }

}