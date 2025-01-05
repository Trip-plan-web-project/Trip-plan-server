package project.tripplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TripplanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TripplanApplication.class, args);
	}

}
