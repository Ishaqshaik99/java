import java.sql.*;
import java.util.Scanner;

public class ExpenseTrackerSimpleJDBC {

    // Database connection details
    static final String URL = "jdbc:mysql://localhost:3306/expense_db";
    static final String USER = "root";      // change if needed
    static final String PASSWORD = "1234";  // change to your MySQL password

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n===== Expense Tracker (JDBC) =====");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View All Transactions");
            System.out.println("4. View Summary");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> addTransaction("Income");
                case 2 -> addTransaction("Expense");
                case 3 -> viewTransactions();
                case 4 -> viewSummary();
                case 5 -> System.out.println("Thank you for using Expense Tracker 💰");
                default -> System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 5);
    }

    // ➕ Add Transaction
    public static void addTransaction(String type) {
        System.out.print("Enter amount (₹): ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter category: ");
        String category = sc.nextLine();
        System.out.print("Enter date (DD-MM-YYYY): ");
        String date = sc.nextLine();
        System.out.print("Enter note: ");
        String note = sc.nextLine();

        String query = "INSERT INTO transactions (type, amount, category, date, note) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, type);
            ps.setDouble(2, amount);
            ps.setString(3, category);
            ps.setString(4, date);
            ps.setString(5, note);
            ps.executeUpdate();
            System.out.println(type + " added successfully ✅");

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    // 📋 View All Transactions
    public static void viewTransactions() {
        String query = "SELECT * FROM transactions";
        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            System.out.println("ID | Type     | Amount   | Category     | Date       | Note");
            System.out.println("--------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf("%-2d | %-8s | ₹%-8.2f | %-12s | %-10s | %s%n",
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("date"),
                        rs.getString("note"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }

    // 📊 View Summary
    public static void viewSummary() {
        String incomeQuery = "SELECT SUM(amount) AS total_income FROM transactions WHERE type='Income'";
        String expenseQuery = "SELECT SUM(amount) AS total_expense FROM transactions WHERE type='Expense'";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement st = con.createStatement()) {

            double totalIncome = 0, totalExpense = 0;

            ResultSet rsIncome = st.executeQuery(incomeQuery);
            if (rsIncome.next()) totalIncome = rsIncome.getDouble("total_income");

            ResultSet rsExpense = st.executeQuery(expenseQuery);
            if (rsExpense.next()) totalExpense = rsExpense.getDouble("total_expense");

            double savings = totalIncome - totalExpense;

            System.out.println("\n===== Summary =====");
            System.out.println("Total Income  : ₹" + totalIncome);
            System.out.println("Total Expense : ₹" + totalExpense);
            System.out.println("Net Savings   : ₹" + savings);

        } catch (SQLException e) {
            System.out.println("Error generating summary: " + e.getMessage());
        }
    }
}
