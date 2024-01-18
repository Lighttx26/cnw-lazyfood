package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import lazyfood.demo.models.DAO.ProductDAO;
import lazyfood.demo.models.DAO.UserDAO;
import lazyfood.demo.models.DTO.OrderDTO;
import lazyfood.demo.models.DTO.OrderDetailsDTO;
import lazyfood.demo.models.DTO.ProductInOrderDTO;
import lazyfood.demo.models.Entity.Order;
import lazyfood.demo.models.DAO.OrderDAO;
import lazyfood.demo.models.Entity.ProductInOrder;
import lazyfood.demo.utils.BillCalculator;

public class OrderBO {
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;
    private final ProductDAO productDAO;

    public OrderBO() {
        orderDAO = new OrderDAO();
        userDAO = new UserDAO();
        productDAO = new ProductDAO();
    }

    public List<OrderDTO> getAllOrders() throws SQLException {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        List<Order> orderEntityList = orderDAO.getAllOrders();
        for (Order order : orderEntityList) {
            OrderDTO orderDTO = OrderDTO.convertFromEntity(order);
            orderDTOList.add(orderDTO);
        }

        return orderDTOList;
    }

    public List<OrderDTO> getOrdersByUser(String userId) throws SQLException {
        List<OrderDTO> orderDTOList = new ArrayList<>();
        List<Order> orderEntityList = orderDAO.getOrdersByUser(userId);
        for (Order order : orderEntityList) {
            OrderDTO orderDTO = OrderDTO.convertFromEntity(order);
            orderDTOList.add(orderDTO);
        }

        return orderDTOList;
    }

    public OrderDetailsDTO getOrderDetails(String orderId) {
        Order order = orderDAO.getOrderById(orderId);
        return OrderDetailsDTO.convertFromEntity(order);
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
            productInOrder.setProduct(productDAO.getProductById(productInOrderDTO.ProductId));
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
