package lazyfood.demo.utils;

import lazyfood.demo.models.DTO.ProductInOrderDTO;
import lazyfood.demo.models.Entity.ProductInOrder;

import java.util.List;

public class BillCalculator {
    public static double calculateTotalPrice(List<ProductInOrderDTO> products) {
        double total = 0;
        for (ProductInOrderDTO product : products) {
            total += product.Price * product.Quantity;
        }
        return total;
    }

//    public static double calculateTotalPrice(List<ProductInOrder> products) {
//        double total = 0;
//        for (ProductInOrder product : products) {
//            total += product.getProduct().getPrice() * product.getQuantity();
//        }
//        return total;
//    }
}
