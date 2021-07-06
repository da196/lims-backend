package tz.go.tcra.lims;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import tz.go.tcra.lims.utils.AppUtility;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = { "tz.go.tcra.lims.*" })
public class LimsApplication {

	private final AppUtility utility;

	public LimsApplication(AppUtility utility) {
		this.utility = utility;
	}

	public static void main(String[] args) {
		SpringApplication.run(LimsApplication.class, args);
	}

	@PostConstruct
	private void postConstruct() {

		utility.seeder();
	}

}
