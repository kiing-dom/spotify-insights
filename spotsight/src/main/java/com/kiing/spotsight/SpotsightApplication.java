package com.kiing.spotsight;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import com.kiing.spotsight.cli.SpotifyInsightsCLI;

@SpringBootApplication
public class SpotsightApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpotsightApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {
			SpotifyInsightsCLI cli = ctx.getBean(SpotifyInsightsCLI.class);
			cli.run();
		};
	}

}
