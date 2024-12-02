package com.gendev.streamcombinations.repository;

import com.gendev.streamcombinations.model.main.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}