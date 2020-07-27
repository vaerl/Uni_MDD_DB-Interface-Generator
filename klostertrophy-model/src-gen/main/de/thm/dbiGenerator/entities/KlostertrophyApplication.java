package de.thm.dbiGenerator;

import de.thm.dbiGenerator.entities.Team;
import de.thm.dbiGenerator.entities.TeamRepository;
import de.thm.dbiGenerator.entities.Game;
import de.thm.dbiGenerator.entities.GameRepository;
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

    private static final Logger log = LoggerFactory.getLogger(this.getClass());
    
    private static final String CONTAINER_NAME = "Klostertrophy";
    private static final String CONTAINER_DATABASE_PASSWORD = "1234klostertrophy";
    private static final String CONTAINER_DATABASE_NAME = "trophy";

    public static void main(String[] args) {
        createMySQLContainer(CONTAINER_NAME, CONTAINER_DATABASE_PASSWORD, CONTAINER_DATABASE_NAME);
        startMySQLContainer(CONTAINER_DATABASE_NAME);
     	SpringApplication.run(Application.class, args);
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
    public CommandLineRunner loadData(
    TeamRepository TeamRepository, 
		        GameRepository GameRepository, 
		        AdminRepository AdminRepository
		    ) {
        return (args) -> {
            // TODO generate Values
        };
    }

}
