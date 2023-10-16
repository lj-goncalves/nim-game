package com.coding.kata.demo.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="game_move"
        ,schema="public"
)
public class GameMove {
    private long id;

    private int remainingMatches;

    private int playerNim;

    private int cpuNim;

    private Game game;

    public GameMove() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "remaining_matches", nullable = false)
    public int getRemainingMatches() {
        return remainingMatches;
    }

    public void setRemainingMatches(int remainingMatches) {
        this.remainingMatches = remainingMatches;
    }
    @Column(name = "player_nim", nullable = false)
    public int getPlayerNim() {
        return playerNim;
    }

    public void setPlayerNim(int playerNim) {
        this.playerNim = playerNim;
    }

    @Column(name = "cpu_nim", nullable = false)
    public int getCpuNim() {
        return cpuNim;
    }

    public void setCpuNim(int cpuNim) {
        this.cpuNim = cpuNim;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="game_id", nullable=false)
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
