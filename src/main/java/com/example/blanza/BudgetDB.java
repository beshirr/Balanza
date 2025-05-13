package com.example.blanza;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BudgetDB extends Database {

    public static void insertBudget(Budgeting budget) {
        String sql = SQLLoader.get("insert_budget");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, budget.getCategory());
            pstmt.setDouble(2, budget.getBudgetAmount());
            pstmt.setDouble(3, budget.getActual_spend());
            pstmt.setDouble(4, budget.getRemaining_budget());
            pstmt.setInt(5, budget.getUser_id());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while inserting budget: " + e.getMessage());
        }
    }


    public static void updateBudgetSpend(int budgetId, double actualSpend, double remainingBudget) {
        String sql = SQLLoader.get("update_budget_spend");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, actualSpend);
            pstmt.setDouble(2, remainingBudget);
            pstmt.setInt(3, budgetId);
            pstmt.executeUpdate(); // Execute the update statement

        } catch (SQLException e) {
            System.out.println("Error while updating budget spend: " + e.getMessage());
        }
    }

    // Retrieve budgets by user ID
    public static void getBudgetsByUserId(int userId) {
        String sql = SQLLoader.get("select_budget_by_user_id");

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

                String category = rs.getString("category");
                double budgetAmount = rs.getDouble("budget_amount");
                double actualSpend = rs.getDouble("actual_spend");
                double remainingBudget = rs.getDouble("remaining_budget");
                System.out.println("Category: " + category + ", Budget Amount: " + budgetAmount +
                        ", Actual Spend: " + actualSpend + ", Remaining Budget: " + remainingBudget);
            }

        } catch (SQLException e) {
            System.out.println("Error while retrieving budgets: " + e.getMessage());
        }
    }

    public static void updateBudget(Budgeting budget) {
        String sql = SQLLoader.get("update_budget");  // Query to update budget

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, budget.getActual_spend());
            pstmt.setDouble(2, budget.getRemaining_budget());
            pstmt.setString(3, budget.getCategory());
            pstmt.setInt(4, budget.getUser_id());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error while updating budget: " + e.getMessage());
        }
    }

    public static Budgeting getBudgetByCategory(String category, int userId) {
        String sql = SQLLoader.get("select_budget_by_category_and_user");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, category);
            pstmt.setInt(2, userId);
            ResultSet rs = pstmt.executeQuery();  //

            if (rs.next()) {

                String budgetCategory = rs.getString("category");
                double budgetAmount = rs.getDouble("budget_amount");
                double actualSpend = rs.getDouble("actual_spend");
                double remainingBudget = rs.getDouble("remaining_budget");

                Budgeting budget = new Budgeting(budgetCategory, budgetAmount, actualSpend, userId);
                budget.setRemaining_budget(remainingBudget);
                return budget;
            }

        } catch (SQLException e) {
            System.out.println("Error while retrieving budget: " + e.getMessage());
        }

        return null;
    }


}
