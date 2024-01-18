package lazyfood.demo.models.DTO;

import lazyfood.demo.models.Entity.Order;
import lazyfood.demo.models.Entity.ProductInOrder;
import lazyfood.demo.utils.BillCalculator;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsDTO {
    public OrderDTO _Order;
    public List<ProductInOrderDTO> Products;
    public double TotalPrice;

    public static OrderDetailsDTO convertFromEntity(Order order) {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();

        orderDetailsDTO._Order = OrderDTO.convertFromEntity(order);

        List<ProductInOrderDTO> productInOrderDTOList = new ArrayList<>();
        for (ProductInOrder productInOrder : order.getProducts()) {
            ProductInOrderDTO productInOrderDTO = ProductInOrderDTO.convertFromEntity(productInOrder);
            productInOrderDTOList.add(productInOrderDTO);
        }
        orderDetailsDTO.Products = productInOrderDTOList;

        orderDetailsDTO.TotalPrice = BillCalculator.calculateTotalPrice(orderDetailsDTO.Products);

        return orderDetailsDTO;
    }
}
