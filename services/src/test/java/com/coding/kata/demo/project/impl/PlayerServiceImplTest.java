package com.coding.kata.demo.project.impl;

import com.coding.kata.demo.project.GameService;
import com.coding.kata.demo.project.dto.RegisterRequestDTO;
import com.coding.kata.demo.project.entity.Game;
import com.coding.kata.demo.project.entity.Player;
import com.coding.kata.demo.project.enums.StatusEnum;
import com.coding.kata.demo.project.exceptions.UsernameNotUniqueException;
import com.coding.kata.demo.project.repository.PlayerRepository;
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

import static org.mockito.Mockito.verify;

public class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private GameService gameService;

    @Mock
    private PlayerRepository playerRepository;

    @Captor
    ArgumentCaptor<Player> playerCaptor;

    private Player player;

    private AutoCloseable closeable;

    @Before
    public void initMocks() {
        closeable = MockitoAnnotations.openMocks(this);
        player = new Player();
        player.setId(1);
        player.setUsername("playerName1");

    }

    @After
    public void releaseMocks() throws Exception {
        closeable.close();
    }

    @Test
    public void registerNewUserUniqueUsernameTest() {
        String username = "playerName2";
        Mockito.when(playerRepository.findByUsername(username)).thenReturn(null);
        Mockito.when(playerRepository.save(Mockito.any(Player.class))).thenReturn(player);

        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername(username);
        long playerId = playerService.registerNewUser(requestDTO);

        //assert the result of registerNewUser
        Assert.assertEquals(player.getId(), playerId);

        //assert that the save method was called 1 time
        verify(playerRepository, Mockito.times(1)).save(Mockito.any(Player.class));

        //assert that a new game was created for the user
        verify(gameService, Mockito.times(1)).createNewGameForUser(playerCaptor.capture());
        Player playerCapturedValue = playerCaptor.getValue();
        Assert.assertEquals(player.getId(), playerCapturedValue.getId());
        Assert.assertEquals(player.getUsername(), playerCapturedValue.getUsername());

    }


    @Test(expected = UsernameNotUniqueException.class)
    public void registerNewUserNonUniqueUsernameTest() {
        String username = player.getUsername();

        Mockito.when(playerRepository.findByUsername(username)).thenReturn(player);
        Mockito.when(playerRepository.save(Mockito.any(Player.class))).thenReturn(player);

        RegisterRequestDTO requestDTO = new RegisterRequestDTO();
        requestDTO.setUsername(username);
        playerService.registerNewUser(requestDTO);
    }
}