package com.coding.kata.demo.project.repository;

import com.coding.kata.demo.project.entity.GameMove;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<GameMove, Long> {
}
