package com.coding.kata.demo.project.repository;

import com.coding.kata.demo.project.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    Game findByPlayerUsernameAndStatus(String username, String status);

    List<Game> findByPlayerUsernameOrderByIdAsc(String username);

}
