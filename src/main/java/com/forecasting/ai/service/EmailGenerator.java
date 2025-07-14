package com.forecasting.ai.service;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class EmailGenerator {

    public static void sendForecastEmail(String jsonData) throws Exception {
        // Parse JSON and create HTML table
        String htmlTable = createHtmlTableFromJson(jsonData);

        // Email configuration (replace with your actual settings)
        String from = "manu567999@gmail.com";
        String host = "smtp.gmail.com";
        String username = "manu567999@gmail.com";
        String password = "rntqewbdkfqkjblj";
        String recipient = "lmanojkum99@gmail.com";

        // Send email
        sendEmailWithTable(recipient, from, host, username, password,
                "Monthly Product Forecast", htmlTable);
    }

    private static String createHtmlTableFromJson(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray products = extractProductsArray(jsonObject);

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: Arial, sans-serif;'>");
        html.append("<h2 style='color: #2c3e50;'>Product Sales Forecast</h2>");

        // Table with improved styling
        html.append("<table style='border-collapse: collapse; width: 100%; margin-bottom: 20px;'>");
        html.append("<thead><tr style='background-color: #3498db; color: white;'>");
        html.append("<th style='padding: 12px; text-align: left;'>Product</th>");
        html.append("<th style='padding: 12px; text-align: left;'>Forecast</th>");
        html.append("<th style='padding: 12px; text-align: left;'>Observations</th>");
        html.append("</tr></thead><tbody>");

        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            // Alternate row colors for better readability
            String rowColor = i % 2 == 0 ? "#ffffff" : "#f8f9fa";

            html.append("<tr style='background-color: ").append(rowColor).append(";'>");
            html.append("<td style='padding: 10px; border-bottom: 1px solid #ddd;'>")
                    .append(product.optString("product_name", "N/A")).append("</td>");
            html.append("<td style='padding: 10px; border-bottom: 1px solid #ddd;'>")
                    .append(product.optInt("predicted_quantity", 0)).append("</td>");
            html.append("<td style='padding: 10px; border-bottom: 1px solid #ddd;'>")
                    .append(product.optString("key_observation", "No observations")).append("</td>");
            html.append("</tr>");
        }

        html.append("</tbody></table>");
        html.append("<p style='color: #7f8c8d; font-size: 0.9em;'>Generated on ")
                .append(java.time.LocalDateTime.now()).append("</p>");
        html.append("</body></html>");

        return html.toString();
    }

    private static JSONArray extractProductsArray(JSONObject jsonObject) throws JSONException {
        Object forecastObj = jsonObject.get("forecast");

        if (forecastObj instanceof JSONObject) {
            JSONObject forecast = (JSONObject) forecastObj;

            // Case 1: Contains month keys like "2025-06"
            Iterator<String> keys = forecast.keys();
            if (keys.hasNext()) {
                String firstKey = keys.next();
                if (firstKey.matches("\\d{4}-\\d{2}")) { // Check if key is in YYYY-MM format
                    return forecast.getJSONArray(firstKey);
                }
            }

            // Case 2: Contains "products" array
            if (forecast.has("products")) {
                return forecast.getJSONArray("products");
            }

            // Case 3: Other object structure - try to find first array
            for (String key : forecast.keySet()) {
                Object value = forecast.get(key);
                if (value instanceof JSONArray) {
                    return (JSONArray) value;
                }
            }
        }
        else if (forecastObj instanceof JSONArray) {
            // Direct array format
            return (JSONArray) forecastObj;
        }

        throw new JSONException("Could not identify products array in forecast data");
    }

    private static void sendEmailWithTable(String to, String from, String host,
                                           String username, String password,
                                           String subject, String htmlContent) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(htmlContent, "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw e;
        }
    }
}