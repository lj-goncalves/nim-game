package com.coding.kata.demo.project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="game"
        ,schema="public"
)
public class Game implements java.io.Serializable {
    private long id;

    private int remainingMatches;

    private int playerLastNim;

    private int cpuLastNim;

    private String status;

    private String winner;

    private Date lastModifiedTs;

    private Player player;

    private int numberOfMatchesPlayerCanTake;

    private Set<GameMove> historyEntries = new HashSet<>(0);

    public Game() {
    }

    public Game(int remainingMatches, int playerLastNim, int cpuLastNim, String status, String winner, Date lastModifiedTs, Player player, int numberOfMatchesPlayerCanTake) {
        this.remainingMatches = remainingMatches;
        this.playerLastNim = playerLastNim;
        this.cpuLastNim = cpuLastNim;
        this.status = status;
        this.winner = winner;
        this.lastModifiedTs = lastModifiedTs;
        this.player = player;
        this.numberOfMatchesPlayerCanTake = numberOfMatchesPlayerCanTake;
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
    @Column(name = "player_last_nim", nullable = false)
    public int getPlayerLastNim() {
        return playerLastNim;
    }

    public void setPlayerLastNim(int playerLastNim) {
        this.playerLastNim = playerLastNim;
    }

    @Column(name = "cpu_last_nim", nullable = false)
    public int getCpuLastNim() {
        return cpuLastNim;
    }

    public void setCpuLastNim(int cpuLastNim) {
        this.cpuLastNim = cpuLastNim;
    }

    @Column(name = "status", nullable = false, length = 16)
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "winner", length = 16)
    public String getWinner() {
        return this.winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modified_ts", length = 29)
    public Date getLastModifiedTs() {
        return this.lastModifiedTs;
    }

    public void setLastModifiedTs(Date lastModifiedTs) {
        this.lastModifiedTs = lastModifiedTs;
    }

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="player_id", nullable=false)
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Column(name = "matches_player_can_take", nullable = false)
    public int getNumberOfMatchesPlayerCanTake() {
        return numberOfMatchesPlayerCanTake;
    }

    public void setNumberOfMatchesPlayerCanTake(int numberOfMatchesPlayerCanTake) {
        this.numberOfMatchesPlayerCanTake = numberOfMatchesPlayerCanTake;
    }

    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.LAZY, mappedBy="game")
    public Set<GameMove> getHistoryEntries() {
        return historyEntries;
    }

    public void setHistoryEntries(Set<GameMove> historyEntries) {
        this.historyEntries = historyEntries;
    }

}
