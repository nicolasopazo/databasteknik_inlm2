package app;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderUtil {

    public void viewAvailableShoes(Connection connection) {
        Statement statement;
        ResultSet resultSet;
        System.out.println(" List of all shoes in the shop:");
        System.out.println("------------------------------------------------------------------");
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("select name, color, size, price, in_stock from shoe");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String color = resultSet.getString("color");
                int size = resultSet.getInt("size");
                double price = resultSet.getDouble("price");
                int inStock = resultSet.getInt("in_stock");
                System.out.println(" " + name + ", Color: " + color + ", Size: " + size + ", Price: " + price + "kr" + ", In stock: " + inStock );
            }
            System.out.println("------------------------------------------------------------------");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addToCart(Connection connection, int customerId, int orderId, String shoeName) {
        CallableStatement callableStatement;
        ResultSet resultSet;
        int shoeId = getShoeId(shoeName, connection);

        try {
            callableStatement = connection.prepareCall("call Skoshop.AddToCart(?,?,?)");
            callableStatement.setInt(1,customerId);
            callableStatement.setInt(2,orderId);
            callableStatement.setInt(3,shoeId);
            callableStatement.execute();
            resultSet = callableStatement.getResultSet();
            while (resultSet.next()) {
                System.out.println(resultSet.getString("complete")
                );
                System.out.println(" ");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("--------------");
            return false;
        } return true;
    }

        public int getShoeId(String shoeName, Connection connection) {
            try {
                String query =
                        "select id from shoe where name = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, shoeName);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return -1;
        }

    public int getLastOrder(Connection connection) {
        List<Integer> ids = new ArrayList<>();
        try {
            String query =
                    "select id from shoeOrder";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                ids.add(id);
            } return Collections.max(ids);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void viewCart(int orderId, Connection connection) {
        System.out.println(" In your cart: ");
        try {
            String query =
                    "select name, color, size, price from shoe \n" +
                            "inner join mapshoeorder on shoe.id = shoeid where orderid = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String color = resultSet.getString("color");
                int size = resultSet.getInt("size");
                double price = resultSet.getDouble("price");
                System.out.println(" " + name + ", Color: " + color + ", Size: " + size + ", Price: " + price + "kr");
            }
            System.out.println("--------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
