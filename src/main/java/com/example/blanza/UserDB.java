package com.example.blanza;

import java.sql.*;

/**
 * The type User db.
 */
public class UserDB extends Database {
    /**
     * Insert user db.
     *
     * @param username    the username
     * @param email       the email
     * @param phoneNumber the phone number
     * @param password    the password
     */
    public static void insertUserDB(String username, String email, String phoneNumber, String password) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            if (conn != null) {
                String sql = SQLLoader.get("insert_user");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, email);
                stmt.setString(3, phoneNumber);
                stmt.setString(4, password);
                stmt.setString(5, null);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Remove user db.
     *
     * @param email the email
     */
    public static void removeUserDB(String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            if (conn != null) {
                String sql = SQLLoader.get("remove_user");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    /**
     * Gets user info by username.
     *
     * @param username the username
     * @return the user info by username
     */
    public static UserInfo getUserInfoByUsername(String username) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            if (conn != null) {
                String sql = SQLLoader.get("get_user_info_by_username");
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                ResultSet userRecord = stmt.executeQuery();
                if (userRecord.next()) {
                    return new UserInfo(
                            userRecord.getInt("id"),
                            userRecord.getString("username"),
                            userRecord.getString("email"),
                            userRecord.getString("phoneNumber"),
                            userRecord.getString("password"),
                            userRecord.getString("otp")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Gets user info by email.
     *
     * @param email the email
     * @return the user info by email
     */
    public static UserInfo getUserInfoByEmail(String email) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = SQLLoader.get("get_user_info_by_email");
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email);
                ResultSet userRecord = stmt.executeQuery();
                if (userRecord.next()){
                    return new UserInfo(
                            userRecord.getInt("id"),
                            userRecord.getString("username"),
                            userRecord.getString("email"),
                            userRecord.getString("phoneNumber"),
                            userRecord.getString("password"),
                            userRecord.getString("otp")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Sets otp.
     *
     * @param email the email
     * @param otp   the otp
     */
    public static void setOTP(String email, String otp) {
        try (Connection conn = DriverManager.getConnection(DB_URL)){
            String sql = SQLLoader.get("set_otp");
            if (conn != null) {
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, otp);
                stmt.setString(2, email);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}