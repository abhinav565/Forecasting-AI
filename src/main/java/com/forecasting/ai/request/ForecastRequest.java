package com.forecasting.ai.request;


import org.json.JSONObject;

public class ForecastRequest {
    private JSONObject prompt;

    // Getter and Setter
    public JSONObject getPrompt() {
        return prompt;
    }
    public void setPrompt(JSONObject prompt) {
        this.prompt = prompt;
    }
}

