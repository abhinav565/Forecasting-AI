package com.forecasting.ai.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.forecasting.ai.DAO.ForeCastDAO;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {


//    @Value("${gemini.api.url}")
//    private String apiUrl;
//    @Value("${gemini.api.key}")
//    private String apiKey;

    public static String getForecast(boolean isScheduler, String prompt, String user) throws Exception {

    String dataBase=ForeCastDAO.callDAO(user);
    System.out.println("PostGres Call Successful");
    boolean isChat=!prompt.equals("")?true:false;
    //https://console.cloud.google.com/apis/api/generativelanguage.googleapis.com/quotas?inv=1&invt=AbyMVg&project=gen-lang-client-0277743407
    String apiKey = "AIzaSyCpRx2rRxH9Gevoow4o-AOvFygGp0ogoNw";
    if(!isChat) {
//        prompt = isScheduler ? "by using above data Can you give forecast summary using this data only html no extra text starts from <html> and ends with </html>" : "Can you give me forecast in json for next month, product name and key observation";
        prompt ="Can you give me forecast in json for next month in this format({\n" +
                "  \"forecast\": {\n" +
                "    \"yyyy-mm\": [\n" +
                "      {\n" +
                "        \"product_name\":"  +
                "        \"predicted_quantity\": 10,\n" +
                "        \"key_observation\":";

    }
    System.out.println("Your Prompt "+prompt);
    String promptWithData = dataBase+prompt;

    String requestBody = String.format("""
        {
            "contents": [{
                "parts": [{
                    "text": "%s"
                }]
            }]
        }
        """, promptWithData.replace("\"", "\\\""));

    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + apiKey))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
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


    text = !isChat? text.substring(8, text.length() - 3) : text;
    System.out.println(text);
    if(isScheduler) {
//        EmailSender.sendMail(text);
//        TestEmail.sendJsonAsTableEmail(text);
        EmailGenerator.sendForecastEmail(text);
        System.out.println("Email Sent Successfully");
    }

    return text;
    }


//        System.out.println("Response: " + text);
//        EmailSender.sendMail(text);
    }
