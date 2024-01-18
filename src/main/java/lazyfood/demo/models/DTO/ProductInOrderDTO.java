package lazyfood.demo.models.DTO;

import lazyfood.demo.models.Entity.ProductInOrder;

public class ProductInOrderDTO {
    public String ProductId;
    public String ProductName;
    public double Price;
    public int Quantity;

    public static ProductInOrderDTO convertFromEntity(ProductInOrder productInOrder) {
        ProductInOrderDTO productInOrderDTO = new ProductInOrderDTO();
        productInOrderDTO.ProductId = productInOrder.getProduct().getProductId();
        productInOrderDTO.ProductName = productInOrder.getProduct().getProductName();
        productInOrderDTO.Price = productInOrder.getProduct().getPrice();
        productInOrderDTO.Quantity = productInOrder.getQuantity();
        return productInOrderDTO;
    }
}
