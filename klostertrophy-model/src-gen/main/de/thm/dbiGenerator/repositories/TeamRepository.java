package de.thm.dbiGenerator.repositories;

import de.thm.dbiGenerator.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAll();
	
	// ONLY do this if property name exists
	List<Team> findByNameStartsWithIgnoreCase(String name);
}
