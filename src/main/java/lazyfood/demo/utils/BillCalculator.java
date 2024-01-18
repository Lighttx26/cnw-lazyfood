package lazyfood.demo.utils;

import lazyfood.demo.models.DTO.ProductInOrderDTO;

import java.util.List;

public class BillCalculator {
    public static double calculateTotalPrice(List<ProductInOrderDTO> products) {
        double total = 0;
        for (ProductInOrderDTO product : products) {
            total += product.Price * product.Quantity;
        }
        return total;
    }
}
