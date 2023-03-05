package app;

import java.sql.*;
import java.util.Scanner;

public class CustomerUtil {

    Scanner scanner = new Scanner(System.in);
    OrderUtil orderUtil = new OrderUtil();

    public void login(Connection connection) {
        System.out.println("Welcome to Skoshop");
        System.out.println("Log in (use email address as username)\n");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        int orderId = 0;

        int loginId = customerValidation(username, password, connection);
        if (loginId > 0) {
            System.out.println("\n Welcome " + username + "\n");
            while (true) {
                System.out.println(
                        "1. View all available shoes\n" +
                        "2. Add to cart\n" +
                        "3. View cart\n" +
                        "4. Exit\n" + "--------------\n" +
                        "Enter your choice: ");

                int choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        System.out.println(loginId);
                        orderUtil.viewAvailableShoes(connection);
                        break;
                    case 2:
                        System.out.println("Add an item to your cart: ");
                        boolean completed = orderUtil.addToCart(connection,loginId,orderId,(scanner.nextLine()));
                        if (completed) {
                            orderId = orderUtil.getLastOrder(connection);
                        }
                        break;
                    case 3:
                        orderUtil.viewCart(orderId, connection);
                        break;
                    case 4:
                        System.out.println("Quitting program...");
                        return;

                    default: System.out.println("invalid choice");
                        }
                }
        } else {
            System.out.println("Invalid email address or password.");
        }
    }

    public int customerValidation(String username, String password, Connection connection) {
        try {
            String query =
                    "select id from customer where email = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
