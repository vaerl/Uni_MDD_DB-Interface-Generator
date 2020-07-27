package de.thm.dbiGenerator;

import de.thm.dbiGenerator.entities.Team;
import de.thm.dbiGenerator.entities.Game;
import de.thm.dbiGenerator.entities.Admin;
import de.thm.dbiGenerator.repositories.TeamRepository;
import de.thm.dbiGenerator.repositories.GameRepository;
import de.thm.dbiGenerator.repositories.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.HashSet;

@SpringBootApplication
public class KlostertrophyApplication {

    private static final Logger log = LoggerFactory.getLogger(KlostertrophyApplication.class);
    
    private static final String CONTAINER_NAME = "Klostertrophy";
    private static final String CONTAINER_DATABASE_PASSWORD = "1234klostertrophy";
    private static final String CONTAINER_DATABASE_NAME = "trophy";

    public static void main(String[] args) {
        createMySQLContainer(CONTAINER_NAME, CONTAINER_DATABASE_PASSWORD, CONTAINER_DATABASE_NAME);
        startMySQLContainer(CONTAINER_DATABASE_NAME);
     	SpringApplication.run(KlostertrophyApplication.class, args);
    }

    public static void createMySQLContainer(String containerName, String databasePassword, String databaseName) {
            try {
                log.info("Checking if container {} exists.", containerName);
                Process check = Runtime.getRuntime().exec("docker inspect -f '{{.State.Running}}' " + containerName);
                String res = String.valueOf(check.getInputStream());
                log.info("Container exists: {}", res);
                check.getOutputStream().close();
                if (!res.contains("true")) {
                    log.info("Creating container {}.", containerName);
                    Process run = Runtime.getRuntime()
                            .exec("docker run -p 3306:3306 --name " + containerName + " -e MYSQL_ROOT_PASSWORD="
                                    + databasePassword + " -e MYSQL_DATABASE=" + databaseName + " -d mysql:latest");
                    run.getOutputStream().close();
                    log.info("Created docker-container with name: {}", containerName);
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Could not create docker-container with name: {}", containerName);
            }
        }
    
        private static void startMySQLContainer(String containerName) {
            try {
                Process start = Runtime.getRuntime().exec("docker start " + containerName);
                start.getOutputStream().close();
                log.info("Started docker-container with name: {}", containerName);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Could'nt start docker-container with name: {}", containerName);
            }
        }

    @Bean
    public CommandLineRunner loadData(TeamRepository teamRepository, 
    GameRepository gameRepository, 
    AdminRepository adminRepository
    ) {
        return (args) -> {
            Team team1 = new Team();
            team1.setStatus(Team.Status.DONE);
            team1.setName("asdf");
            team1.setPoints(-1);
            team1.setGender(Team.Gender.MALE);
            teamRepository.save(team1);
            
            Game game2 = new Game();
            game2.setName("asdf");
            game2.setStatus(Game.Status.DONE);
            game2.setSortOrder(Game.SortOrder.ASCENDING);
            game2.setPointType(Game.PointType.TIME);
            gameRepository.save(game2);
            
            Admin admin3 = new Admin();
            admin3.setUsername("asdf");
            admin3.setPassword("asdf");
            adminRepository.save(admin3);
            
        };
    }

}
