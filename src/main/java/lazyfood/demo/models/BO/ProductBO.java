package lazyfood.demo.models.BO;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import lazyfood.demo.models.Entity.Product;
import lazyfood.demo.models.DAO.ProductDAO;

public class ProductBO {
    private ProductDAO productDAO;

    public ProductBO() {
        productDAO = new ProductDAO();
    }

    public ArrayList<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }

    public Product getProductById(String id) throws SQLException {
        return productDAO.getProductById(id);
    }

    // public ArrayList<Product> getProductByName(String name) {
    // return productDAO.filterProduct(name, "ProductName");
    // }

    // public ArrayList<Product> getProductByCategoryId(String id) {
    // return productDAO.filterProduct(id, "CategoryId");
    // }

    public void addProduct(Product product) throws SQLException, IOException {
        if (productDAO.getProductById(product.getProductId()) == null)
            productDAO.addProduct(product);
        else
            throw new SQLIntegrityConstraintViolationException("ProductID is duplicated.");
    }

    public void updateProduct(Product product) throws SQLException, IOException {
        productDAO.updateProduct(product);
    }

    public void deleteProduct(String id) throws SQLException {
        if (productDAO.getProductById(id) != null)
            productDAO.deleteProduct(id);
        else
            throw new SQLException("Product not found");
    }

    public ArrayList<Product> filterProduct(String value, String type) throws SQLException {
        return productDAO.filterProduct(value, type);
    }
}
