package cz.vojtechsika.tennisclub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * The entry point for the Tennis Club application. This class bootstraps the Spring Boot context
 * and starts the application.
 *
 * <p>When the application starts, a message is printed to the console.</p>
 *
 *
 * @author Vojtěch Šika
 * @since 2025-06-01
 */
@SpringBootApplication
public class TennisClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisClubApplication.class, args);

		System.out.println("Tennis club application started ");
	}


}
