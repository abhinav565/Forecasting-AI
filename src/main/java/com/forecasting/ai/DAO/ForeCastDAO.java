package com.forecasting.ai.DAO;

import java.sql.*;

public class ForeCastDAO {

    // Database connection parameters - update these with your details
    private static final String URL = "jdbc:postgresql://database-1.cfcoiu6cowe0.ap-south-1.rds.amazonaws.com:5432/postgres";
    private static final String USER = "Scott";
    private static final String PASSWORD = "Tiger12345";
//    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
//    private static final String USER = "postgres";
//    private static final String PASSWORD = "password";

    public static String callDAO(String shopId) {
        StringBuilder sb = new StringBuilder();
        // Basic SELECT query
        if (shopId == null || shopId.isEmpty()) {
            shopId="1";
        }
        String sql = "SELECT TO_CHAR(pi.created_at, 'YYYY-MM') AS year_month,pi.quantity,p.name AS product_name FROM product_in pi JOIN product p ON pi.prod_id = p.id where type='OUT' and shop_id="+shopId;
//                +"\tEXTRACT(YEAR FROM sm.date) = 2024\n" +
//                "    AND EXTRACT(MONTH FROM sm.date) = 12;";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            //rs to string
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Append column headers
            for (int i = 1; i <= columnCount; i++) {
                sb.append(metaData.getColumnName(i));
                if (i < columnCount) sb.append(", ");
            }
            sb.append("\n");

            // Append rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    sb.append(resultSet.getString(i));
                    if (i < columnCount) sb.append(", ");
                }
                sb.append("\n");
            }


            System.out.println("Connected to PostgreSQL database!\n");

            // Process the result set
//            while (resultSet.next()) {
//                int id = resultSet.getInt("id");
//                String name = resultSet.getString("name");
//                String email = resultSet.getString("email");
//
//                System.out.printf("ID: %d, Name: %s, Email: %s%n", id, name, email);
//            }

        } catch (SQLException e) {
            System.err.println("Database connection error:");
            e.printStackTrace();
        }
        return sb.toString();
    }
}

