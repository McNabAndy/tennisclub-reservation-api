package cz.vojtechsika.tennisclub;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class TennisClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(TennisClubApplication.class, args);

		System.out.println("Tennis club application started 1");
	}





}
