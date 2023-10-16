package com.coding.kata.demo.project.repository;

import com.coding.kata.demo.project.entity.Player;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByUsername(String username);

    boolean existsPlayerByUsername(String username);
}
