package com.example.blanza;

import java.sql.*;

public class UserDB {

    public static void createUserDB() {
        String DB_URL = "jdbc:sqlite:Balanza.db";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                        + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + " username TEXT NOT NULL UNIQUE,\n"
                        + " email TEXT NOT NULL UNIQUE,\n"
                        + " phoneNumber TEXT NOT NULL UNIQUE,\n"
                        + " password TEXT NOT NULL\n"
                        + ");";

                Statement stmt = conn.createStatement();
                stmt.execute(sql);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertUserDB(String username, String email, String phoneNumber, String password) {
        String DB_URL = "jdbc:sqlite:Balanza.db";
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            if (conn != null) {
                String sql = "INSERT INTO users (username, email, phoneNumber, password) \n"
                        + "VALUES(?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, password);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void removeUserDB(String username) {
        String DB_URL = "jdbc:sqlite:Balanza.db";
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            if (conn != null) {
                String sql = "DELETE FROM users WHERE username = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static UserInfo getUserInfoByUsername(String username) {
        String DB_URL = "jdbc:sqlite:Balanza.db";
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            if (conn != null) {
                String sql = "SELECT * FROM users WHERE username = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet userRecord = stmt.executeQuery();
                if (userRecord.next()) {
                    return new UserInfo(
                            userRecord.getInt("id"),
                            userRecord.getString("username"),
                            userRecord.getString("email"),
                            userRecord.getString("phoneNumber"),
                            userRecord.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
