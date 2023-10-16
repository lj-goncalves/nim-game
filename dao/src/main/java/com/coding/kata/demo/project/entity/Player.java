package com.coding.kata.demo.project.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="player"
        ,schema="public"
)
public class Player {

    private long id;

    private String username;

    private Set<Game> games = new HashSet<>(0);

    public Player() {
    }

    public Player(String username) {
        this.username = username;
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

    @Column(name = "username", unique = true, nullable = false, length = 16)
    public String getUsername() {
        return username;
    }

    public void setUsername(String propertyKey) {
        this.username = propertyKey;
    }

    @OneToMany(cascade= CascadeType.ALL, fetch= FetchType.LAZY, mappedBy="player")
    public Set<Game> getGames() {
        return games;
    }

    public void setGames(Set<Game> games) {
        this.games = games;
    }
}
