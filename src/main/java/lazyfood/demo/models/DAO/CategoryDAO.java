package lazyfood.demo.models.DAO;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import lazyfood.demo.models.Entity.Category;

public class CategoryDAO {
    public ArrayList<Category> getAllCategories() throws SQLException {
        ArrayList<Category> categories = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT CategoryId, CategoryName FROM category";
            Statement statement = conn.createStatement();
            ResultSet res = statement.executeQuery(query);
            while (res.next()) {
                categories.add(new Category(res.getString("CategoryId"),
                        res.getString("CategoryName")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at CategoryDAO.getAllCategories(): " + e.getMessage());
        }

        return categories;
    }

    public Category getCategoryById(String id) throws SQLException {
        Category category = null;

        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT CategoryId, CategoryName FROM category WHERE CategoryId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);
            ResultSet res = statement.executeQuery(query);
            while (res.next()) {
                category = new Category(
                        res.getString("CategoryId"),
                        res.getString("CategoryName"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at CategoryDAO.getCategoryById(): " + e.getMessage());
        }

        return category;
    }

    public void addCategory(Category category) throws SQLException {
        try (Connection conn = DBConnector.getConnection()) {
            String query = "INSERT INTO category VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, category.getCategoryId());
            statement.setString(2, category.getCategoryName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at CategoryDAO.addCategory(): " + e.getMessage());
        }
    }

    public void updateCategory(Category category) throws SQLException, IOException {
        try (Connection conn = DBConnector.getConnection()) {
            String query = "UPDATE category SET CategoryName = ? WHERE CategoryId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, category.getCategoryName());
            statement.setString(2, category.getCategoryId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at CategoryDAO.updateCategory(): " + e.getMessage());
        }

        // return true;
    }

    public void deleteCategory(String id) throws SQLException {
        try (Connection conn = DBConnector.getConnection()) {
            String query = "DELETE FROM category WHERE CategoryId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at CategoryDAO.deleteCategory(): " + e.getMessage());
        }

        // return true;
    }
}
