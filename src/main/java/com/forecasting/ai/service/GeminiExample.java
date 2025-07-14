package com.forecasting.ai;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;

import org.json.JSONArray;
import org.json.JSONObject;


public class GeminiExample {
    public static void main(String[] args) throws Exception {
        //https://console.cloud.google.com/apis/api/generativelanguage.googleapis.com/quotas?inv=1&invt=AbyMVg&project=gen-lang-client-0277743407
        String apiKey = "AIzaSyCpRx2rRxH9Gevoow4o-AOvFygGp0ogoNw";
        String prompt = "Product\tMonth\tQuantity\n" +
                "Monitor\tOct\t10\n" +
                "KeyboardOct\t17\n" +
                "Phone\tDec\t34\n" +
                "Laptop\tMarch\t24\n" +
                "Tablet\tMay\t34\n" +
                "Laptop\tApr\t52\n" +
                "Tablet\tJan\t51\n" +
                "Monitor\tMarch\t81\n" +
                "Phone\tAug\t35\n" + "Can you give summary for this data";
        
        String requestBody = String.format("""
        {
            "contents": [{
                "parts": [{
                    "text": "%s"
                }]
            }]
        }
        """, prompt.replace("\"", "\\\""));
        
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(requestBody))
                .build();
        
        HttpResponse<String> response = client.send(
            request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        String text = json
        		.getJSONArray("candidates")
        		.getJSONObject(0)
        		.getJSONObject("content")
        		.getJSONArray("parts")
        		.getJSONObject(0)
        		.getString("text");

        
        System.out.println("Response: " + text);
//        EmailSender.sendMail(text);
    }
}