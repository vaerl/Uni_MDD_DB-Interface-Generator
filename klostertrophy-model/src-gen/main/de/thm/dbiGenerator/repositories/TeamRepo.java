package de.thm.dbiGenerator.backend.repos;

import de.thm.dbiGenerator.backend.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findAll();

    List<Team> findByNameStartsWithIgnoreCase(String name);

    List<Team> findByDoneIs(boolean b);

}
