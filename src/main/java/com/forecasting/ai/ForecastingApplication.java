package com.forecasting.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class ForecastingApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForecastingApplication.class, args);
	}

}
