package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lazyfood.demo.models.DAO.UserDAO;
import lazyfood.demo.models.DTO.OrderDTO;
import lazyfood.demo.models.DTO.OrderDetailsDTO;
import lazyfood.demo.models.DTO.ProductInOrderDTO;
import lazyfood.demo.models.Entity.Order;
import lazyfood.demo.models.DAO.OrderDAO;
import lazyfood.demo.models.Entity.ProductInOrder;

public class OrderBO {
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;

    public OrderBO() {
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
    }

    public List<OrderDTO> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public List<OrderDTO> getOrdersByUser(String userId) throws SQLException {
        return orderDAO.getOrdersByUser(userId);
    }

    public OrderDetailsDTO getOrderById(String orderId) {
        return orderDAO.getOrderById(orderId);
    }

    public void createOrder(OrderDetailsDTO orderDTO) {
        Order order = new Order();
        order.setOrderId(orderDTO._Order.OrderId);
        order.setAddress(orderDTO._Order.Address);
        order.setPhoneNumber(orderDTO._Order.PhoneNumber);
        order.setTime(orderDTO._Order.OrderDatetime);
        order.setIsDelivered(false);
        order.setCustomer(userDAO.getUserById(orderDTO._Order.CustomerId));

        List<ProductInOrder> products = new ArrayList<>();

        for (ProductInOrderDTO productInOrderDTO : orderDTO.Products) {
            ProductInOrder productInOrder = new ProductInOrder();
            productInOrder.setProduct(ProductBO.getProductById(productInOrderDTO.ProductId));
            productInOrder.setQuantity(productInOrderDTO.Quantity);
            productInOrder.setOrder(order);
            products.add(productInOrder);
        }

        order.setProducts(products);

        orderDAO.createOrder(order);
    }

    public void setOrderDelivered(String orderId, boolean state) {
        orderDAO.setDeliveredState(orderId, state);
    }
}
