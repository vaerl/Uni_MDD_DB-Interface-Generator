package de.thm.dbiGenerator.backend.repos;

import de.thm.dbiGenerator.backend.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAll();

    List<Game> findByNameStartsWithIgnoreCase(String name);

    List<Game> findByDoneIs(boolean b);

}
