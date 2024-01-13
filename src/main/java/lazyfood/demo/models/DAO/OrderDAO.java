package lazyfood.demo.models.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;

import lazyfood.demo.models.Entity.Order;
import lazyfood.demo.models.Entity.ProductInOrder;

public class OrderDAO {

    public ArrayList<Order> getAllOrders() {

        ArrayList<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT `order`.OrderId, `order`.CustomerId, user.FullName, `order`.PhoneNumber, `order`.Address, `order`.Time, `order`.IsDelivered FROM `order` INNER JOIN user on `order`.CustomerId = user.UserId Order BY `order`.Time DESC";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);
            while (res.next()) {
                orders.add(new Order(res.getString("OrderId"),
                        res.getString("CustomerId"),
                        res.getString("FullName"),
                        null,
                        res.getTimestamp("Time").toLocalDateTime(),
                        res.getString("PhoneNumber"),
                        res.getString("Address"),
                        res.getBoolean("IsDelivered")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public ArrayList<Order> getOrdersByUser(String userId) {

        ArrayList<Order> orders = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT `order`.OrderId, `order`.CustomerId, user.FullName, `order`.PhoneNumber, `order`.Address, `order`.Time, `order`.IsDelivered FROM `order` INNER JOIN user on `order`.CustomerId = user.UserId WHERE UserId = ? Order BY `order`.Time DESC";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, userId);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                orders.add(new Order(res.getString("OrderId"),
                        res.getString("CustomerId"),
                        res.getString("FullName"),
                        null,
                        res.getTimestamp("Time").toLocalDateTime(),
                        res.getString("PhoneNumber"),
                        res.getString("Address"),
                        res.getBoolean("IsDelivered")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public Order getOrderById(String orderId) {
        Order order = null;
        ArrayList<ProductInOrder> products = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection()) {
            ProductDAO productDAO = new ProductDAO();

            String query2 = "SELECT `order`.OrderId, `order`.CustomerId, user.FullName, `order`.PhoneNumber, `order`.Address, `order`.Time, `order`.IsDelivered FROM `order` INNER JOIN user on `order`.CustomerId = user.UserId WHERE OrderId = ?";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            statement2.setString(1, orderId);
            ResultSet res2 = statement2.executeQuery();
            while (res2.next()) {
                order = new Order(res2.getString("OrderId"),
                        res2.getString("CustomerId"),
                        res2.getString("FullName"),
                        products,
                        res2.getTimestamp("Time").toLocalDateTime(),
                        res2.getString("PhoneNumber"),
                        res2.getString("Address"),
                        res2.getBoolean("IsDelivered"));
            }

            if (order == null)
                return null;

            String query = "SELECT ProductId, Quantity FROM orderproduct WHERE OrderId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, orderId);
            ResultSet res = statement.executeQuery();
            while (res.next()) {
                products.add(new ProductInOrder(productDAO.getProductById(res.getString("ProductId")),
                        res.getInt("Quantity")));
            }

            order.setProducts(products);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    public void createOrder(Order order) {
        try (Connection conn = DBConnector.getConnection()) {

            String query = "INSERT INTO `order` VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, order.getOrderId());
            statement.setString(2, order.getCustomerId());
            statement.setString(3, order.getPhoneNumber());
            statement.setString(4, order.getAddress());
            statement.setTimestamp(5, Timestamp.valueOf(order.getTime()));
            statement.setBoolean(6, order.isDelivered());

            statement.executeUpdate();

            String query2 = "INSERT INTO orderproduct VALUES (?, ?, ?)";
            PreparedStatement statement2 = conn.prepareStatement(query2);
            for (ProductInOrder product : order.getProducts()) {
                statement2.setString(1, order.getOrderId());
                statement2.setString(2, product.getProductId());
                statement2.setInt(3, product.getQuantity());
                statement2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDeliveredState(String orderId, boolean state) {
        try (Connection conn = DBConnector.getConnection()) {

            String query = "UPDATE `order` SET IsDelivered = ? WHERE OrderId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setBoolean(1, state);
            statement.setString(2, orderId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
