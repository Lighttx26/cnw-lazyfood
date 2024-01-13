package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.util.ArrayList;

import lazyfood.demo.models.Entity.Order;
import lazyfood.demo.models.DAO.OrderDAO;

public class OrderBO {
    private OrderDAO orderDAO;

    public OrderBO() {
        orderDAO = new OrderDAO();
    }

    public ArrayList<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public ArrayList<Order> getOrdersByUser(String userId) throws SQLException {
        return orderDAO.getOrdersByUser(userId);
    }

    public Order getOrderById(String orderId) throws SQLException {
        return orderDAO.getOrderById(orderId);
    }

    public void createOrder(Order order) throws SQLException {
        orderDAO.createOrder(order);
    }

    public void setOrderDelivered(String orderId, boolean state) throws SQLException {
        orderDAO.setDeliveredState(orderId, state);
    }
}
