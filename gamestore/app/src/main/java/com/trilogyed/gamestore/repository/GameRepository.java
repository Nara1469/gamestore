package com.trilogyed.gamestore.repository;

import com.trilogyed.gamestore.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findAllGamesByStudio(String studio);
    List<Game> findAllGamesByEsrbRating(String esrbRating);
    List<Game> findAllGamesByTitle(String title);
}
