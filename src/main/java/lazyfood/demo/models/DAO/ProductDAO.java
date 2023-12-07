package lazyfood.demo.models.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import lazyfood.demo.models.Bean.Product;

import static lazyfood.demo.utils.general.convertBlobToBase64;

public class ProductDAO {

    public ArrayList<Product> getAllProducts() throws SQLException {
        ArrayList<Product> products = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT ProductId, ProductName, product.CategoryId, CategoryName, Price, IsAvailable, Image FROM product inner join category on product.CategoryId = category.CategoryId";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);
            while (res.next()) {

                products.add(new Product(res.getString("ProductId"),
                        res.getString("ProductName"),
                        res.getString("CategoryId"),
                        res.getString("CategoryName"),
                        res.getDouble("Price"),
                        res.getBoolean("IsAvailable"),
                        convertBlobToBase64(res.getBlob("Image"))
                ));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at ProductDAO.getAllProducts(): " + e.getMessage());
        }

        return products;
    }

    public Product getProductById(String id) throws SQLException {
        Product product = null;

        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT ProductId, ProductName, product.CategoryId, CategoryName, Price, IsAvailable, Image FROM product inner join category on product.CategoryId = category.CategoryId where ProductId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);
            ResultSet res = statement.executeQuery(query);
            while (res.next()) {
                product = new Product(res.getString("ProductId"),
                        res.getString("ProductName"),
                        res.getString("CategoryId"),
                        res.getString("CategoryName"),
                        res.getDouble("Price"),
                        res.getBoolean("IsAvailable"),
                        convertBlobToBase64(res.getBlob("Image"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at ProductDAO.getAllProducts(): " + e.getMessage());
        }

        return product;
    }

    public void addProduct(Product product) throws SQLException, IOException{
        try (Connection conn = DBConnector.getConnection()) {
            String query = "INSERT INTO product VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, product.getProductId());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getCategoryId());
            statement.setDouble(4, product.getPrice());
            statement.setBoolean(5, product.isAvailable());
            statement.setString(6, product.getImage());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at ProductDAO.addProduct(): " + e.getMessage());
        }

        // return true;
    }

    public void updateProduct(Product product) throws SQLException, IOException {
        try (Connection conn = DBConnector.getConnection()) {
            String query = "UPDATE product SET ProductName = ?, CategoryId = ?, Price = ?, IsAvailable = ?, ImageUrl = ? WHERE ProductId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(6, product.getProductId());
            statement.setString(1, product.getProductName());
            statement.setString(2, product.getCategoryId());
            statement.setDouble(3, product.getPrice());
            statement.setBoolean(4, product.isAvailable());
            statement.setString(5, product.getImage());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at ProductDAO.updateProduct(): " + e.getMessage());
        }

        // return true;
    }

    public void deleteProduct(String id) throws SQLException {
        try (Connection conn = DBConnector.getConnection()) {
            String query = "DELETE FROM product WHERE ProductId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at ProductDAO.deleteProduct(): " + e.getMessage());
        }

        // return true;
    }
}
