package com.forecasting.ai.service;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class TestEmail {

    public static void sendJsonAsTableEmail(String jsonString) {
        try {
            String toEmail="lmanojkum99@gmail.com";
            String subject="Inventory Forecasting";
            // Convert JSON to HTML table
            String htmlContent = convertJsonToHtmlTable(jsonString);

            // Email configuration
            String host = "smtp.gmail.com";
            String from = "manu567999@gmail.com";
            String password = "rntqewbdkfqkjblj";

            // Set properties
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Get session
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, password);
                }
            });

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Set HTML content
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(htmlContent, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String convertJsonToHtmlTable(String jsonString) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><style>")
                .append("table {border-collapse: collapse; width: 100%;}")
                .append("th, td {border: 1px solid #ddd; padding: 8px; text-align: left;}")
                .append("th {background-color: #f2f2f2;}")
                .append("</style></head><body>");

        try {
            // Check if JSON is an array or object
            if (jsonString.trim().startsWith("[")) {
                JSONArray jsonArray = new JSONArray(jsonString);
                html.append("<h2>JSON Data</h2><table>");

                // Create header row from first object's keys
                if (jsonArray.length() > 0) {
                    JSONObject firstObj = jsonArray.getJSONObject(0);
                    html.append("<tr>");
                    for (String key : firstObj.keySet()) {
                        html.append("<th>").append(key).append("</th>");
                    }
                    html.append("</tr>");

                    // Add data rows
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        html.append("<tr>");
                        for (String key : firstObj.keySet()) {
                            Object value = obj.opt(key);
                            html.append("<td>").append(value != null ? value.toString() : "null").append("</td>");
                        }
                        html.append("</tr>");
                    }
                }

                html.append("</table>");
            } else {
                JSONObject jsonObject = new JSONObject(jsonString);
                html.append("<h2>JSON Data</h2><table>");

                html.append("<tr><th>Key</th><th>Value</th></tr>");
                for (String key : jsonObject.keySet()) {
                    Object value = jsonObject.get(key);
                    html.append("<tr>")
                            .append("<td>").append(key).append("</td>")
                            .append("<td>").append(value.toString()).append("</td>")
                            .append("</tr>");
                }

                html.append("</table>");
            }
        } catch (Exception e) {
            html.append("<p>Error parsing JSON: ").append(e.getMessage()).append("</p>");
            html.append("<pre>").append(jsonString).append("</pre>");
        }

        html.append("</body></html>");
        return html.toString();
    }

//    public static void main(String[] args) {
//        // Example JSON
//        String jsonArray = "[{\"id\":1,\"name\":\"John\",\"email\":\"john@example.com\"},"
//                + "{\"id\":2,\"name\":\"Jane\",\"email\":\"jane@example.com\"}]";
//
//        String jsonObject = "{\"status\":\"success\",\"code\":200,\"message\":\"Data retrieved\"}";
//
//        // Send email with JSON array as table
//        sendJsonAsTableEmail(jsonArray, "lmanojkum99@gmail.com", "JSON Data Report");
//
//        // Send email with JSON object as table
//        sendJsonAsTableEmail(jsonObject, "lmanojkum99@gmail.com", "JSON Status Report");
//    }
}