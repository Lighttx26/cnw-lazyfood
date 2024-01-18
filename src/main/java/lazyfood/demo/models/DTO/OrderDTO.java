package lazyfood.demo.models.DTO;

import lazyfood.demo.models.Entity.Order;

import java.time.LocalDateTime;

public class OrderDTO {
    public String OrderId;
    public String CustomerId;
    public String CustomerName;
    public String Address;
    public String PhoneNumber;
    public LocalDateTime OrderDatetime;
    public boolean IsDelivered;

    public static OrderDTO convertFromEntity(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.OrderId = order.getOrderId();
        orderDTO.CustomerId = order.getCustomer().getUserId();
        orderDTO.CustomerName = order.getCustomer().getFullname();
        orderDTO.Address = order.getAddress();
        orderDTO.PhoneNumber = order.getPhoneNumber();
        orderDTO.OrderDatetime = order.getTime();
        orderDTO.IsDelivered = order.isDelivered();
        return orderDTO;
    }
}
