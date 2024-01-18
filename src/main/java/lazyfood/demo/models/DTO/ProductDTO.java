package lazyfood.demo.models.DTO;

import lazyfood.demo.models.Entity.Product;
import lazyfood.demo.utils.BinaryTypeHandler;

public class ProductDTO {
    public String ProductId;
    public String ProductName;
    public String CategoryId;
    public String CategoryName;
    public double Price;
    public String Image;

    public static ProductDTO convertFromEntity(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.ProductId = product.getProductId();
        productDTO.ProductName = product.getProductName();
        productDTO.CategoryId = product.getCategory().getCategoryId();
        productDTO.CategoryName = product.getCategory().getCategoryName();
        productDTO.Price = product.getPrice();
        productDTO.Image = BinaryTypeHandler.ByteArrayToBase64(product.getImage());
        return productDTO;
    }
}
