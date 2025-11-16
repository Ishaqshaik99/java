import java.util.*;

class User {
    private String accountNumber;
    private String pin;
    private double balance;

    public User(String accountNumber, String pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public boolean withdraw(double amount) {
        if (amount > balance) return false;
        balance -= amount;
        return true;
    }
}

class Bank {
    private Map<String, User> users = new HashMap<>();

    public Bank() {
        // Preloaded sample users
        users.put("1001", new User("1001", "1234", 5000));
        users.put("1002", new User("1002", "5678", 10000));
        users.put("1003", new User("1003", "0000", 1500));
    }

    public User validateUser(String accountNumber, String pin) {
        User user = users.get(accountNumber);
        if (user != null && user.getPin().equals(pin)) {
            return user;
        }
        return null;
    }
}

public class ATM {
    private static Scanner sc = new Scanner(System.in);
    private static Bank bank = new Bank();

    public static void main(String[] args) {
        System.out.println("===== Welcome to Java ATM =====");

        System.out.print("Enter Account Number: ");
        String accountNumber = sc.nextLine();
        System.out.print("Enter PIN: ");
        String pin = sc.nextLine();

        User currentUser = bank.validateUser(accountNumber, pin);
        if (currentUser == null) {
            System.out.println("❌ Invalid Account Number or PIN!");
            return;
        }

        System.out.println("\nLogin Successful ✅\n");
        mainMenu(currentUser);
    }

    public static void mainMenu(User user) {
        int choice;
        do {
            System.out.println("===== ATM Main Menu =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Change PIN");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> checkBalance(user);
                case 2 -> deposit(user);
                case 3 -> withdraw(user);
                case 4 -> changePin(user);
                case 5 -> System.out.println("Thank you for using Java ATM 💳");
                default -> System.out.println("Invalid Choice! Try again.");
            }
            System.out.println();
        } while (choice != 5);
    }

    public static void checkBalance(User user) {
        System.out.println("Your current balance: ₹" + user.getBalance());
    }

    public static void deposit(User user) {
        System.out.print("Enter amount to deposit: ₹");
        double amount = sc.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        user.deposit(amount);
        System.out.println("Deposit Successful ✅");
        System.out.println("Updated Balance: ₹" + user.getBalance());
    }

    public static void withdraw(User user) {
        System.out.print("Enter amount to withdraw: ₹");
        double amount = sc.nextDouble();
        if (amount <= 0) {
            System.out.println("Invalid amount!");
            return;
        }
        if (user.withdraw(amount)) {
            System.out.println("Withdrawal Successful ✅");
            System.out.println("Remaining Balance: ₹" + user.getBalance());
        } else {
            System.out.println("❌ Insufficient Balance!");
        }
    }

    public static void changePin(User user) {
        sc.nextLine(); // consume newline
        System.out.print("Enter Old PIN: ");
        String oldPin = sc.nextLine();
        if (!user.getPin().equals(oldPin)) {
            System.out.println("Incorrect Old PIN ❌");
            return;
        }
        System.out.print("Enter New PIN: ");
        String newPin = sc.nextLine();
        user.setPin(newPin);
        System.out.println("PIN changed successfully ✅");
    }
}
