package com.forecasting.ai;

import com.forecasting.ai.DAO.ForeCastDAO;
import com.forecasting.ai.service.GeminiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ForecastingApplicationTests {

	public static void main(String[] args) throws Exception {
		GeminiService.getForecast(true, "","");
	}
}
