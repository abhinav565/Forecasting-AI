package com.forecasting.ai.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import com.forecasting.ai.service.GeminiService;

import java.io.IOException;
@CrossOrigin
@RestController
@RequestMapping("/api")
public class ForecastingController {

    @Autowired
    private GeminiService geminiService;

    String queryNul="";
    @GetMapping("/chatAI")
    public String searchAI(@RequestParam String shopId ,@RequestParam String query) throws Exception {
        return geminiService.getForecast(false, query, shopId);
    }

    @GetMapping("/getForecast")
    public String generateForecast(@RequestParam String shopId) throws Exception {
        return geminiService.getForecast(false, queryNul, shopId);
    }
    @Scheduled(cron = "0 0 0 25 * ?")
//    @Scheduled(fixedRate = 200000)
    public void scheduledForecast() throws Exception {
        geminiService.getForecast(true, queryNul, null);
    }
    //to generate mail
    @GetMapping("/getMail")
    public String generateMail() throws Exception {
        return geminiService.getForecast(true, queryNul, null);
    }
}

