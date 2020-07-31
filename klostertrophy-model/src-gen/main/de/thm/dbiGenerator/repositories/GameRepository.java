package de.thm.dbiGenerator.repositories;

import de.thm.dbiGenerator.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    List<Game> findAll();
	
	// ONLY do this if property name exists
	List<Game> findByNameStartsWithIgnoreCase(String name);
}
