package de.thkoeln.archilab.fae.GPSMockService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GpsMockServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpsMockServiceApplication.class, args);
	}
}
