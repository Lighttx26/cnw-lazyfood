package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import lazyfood.demo.models.DAO.CategoryDAO;
import lazyfood.demo.models.DTO.ProductDTO;
import lazyfood.demo.models.Entity.Product;
import lazyfood.demo.models.DAO.ProductDAO;
import lazyfood.demo.utils.BinaryTypeHandler;

public class ProductBO {
    private final ProductDAO productDAO;
    private final CategoryDAO categoryDAO;

    public ProductBO() {
        productDAO = new ProductDAO();
        categoryDAO = new CategoryDAO();
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> productEntityList = productDAO.getAllProducts();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productEntityList) {
            ProductDTO productDTO = ProductDTO.convertFromEntity(product);
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    public ProductDTO getProductById(String id) {
        Product product = productDAO.getProductById(id);
        return ProductDTO.convertFromEntity(product);
    }

    // public ArrayList<Product> getProductByName(String name) {
    // return productDAO.filterProduct(name, "ProductName");
    // }

    // public ArrayList<Product> getProductByCategoryId(String id) {
    // return productDAO.filterProduct(id, "CategoryId");
    // }

    public void addProduct(ProductDTO productDTO) throws SQLIntegrityConstraintViolationException {
        if (productDAO.getProductById(productDTO.ProductId) == null) {
            Product product = GetEntityFromDTO(productDTO);
            productDAO.addProduct(product);
        } else
            throw new SQLIntegrityConstraintViolationException("ProductID is duplicated.");
    }

    public void updateProduct(ProductDTO productDTO) {
        Product product = GetEntityFromDTO(productDTO);
        productDAO.updateProduct(product);
    }

    public void deleteProduct(String id) throws SQLException {
        if (productDAO.getProductById(id) != null)
            productDAO.deleteProduct(id);
        else
            throw new SQLException("Product not found");
    }

    private Product GetEntityFromDTO(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductId(productDTO.ProductId);
        product.setProductName(productDTO.ProductName);
        product.setCategory(categoryDAO.getCategoryById(productDTO.CategoryId));             // TODO: Check if category exists
        product.setPrice(productDTO.Price);
        product.setImage(BinaryTypeHandler.Base64ToByteArray(productDTO.Image));
//        product.setAvailable(productDTO.IsAvailable);
        product.setAvailable(true);

        return product;
    }
}

//    public ArrayList<Product> filterProduct(String value, String type) throws SQLException {
//        return productDAO.filterProduct(value, type);
//    }