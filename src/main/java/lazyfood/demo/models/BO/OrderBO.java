package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.util.List;

import lazyfood.demo.models.Entity.Order;
import lazyfood.demo.models.DAO.OrderDAO;

public class OrderBO {
    private final OrderDAO orderDAO;

    public OrderBO() {
        orderDAO = new OrderDAO();
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public List<Order> getOrdersByUser(String userId) throws SQLException {
        return orderDAO.getOrdersByUser(userId);
    }

    public Order getOrderById(String orderId) {
        return orderDAO.getOrderById(orderId);
    }

    public void createOrder(Order order) {
        orderDAO.createOrder(order);
    }

    public void setOrderDelivered(String orderId, boolean state) {
        orderDAO.setDeliveredState(orderId, state);
    }
}
