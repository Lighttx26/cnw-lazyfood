package lazyfood.demo.models.BO;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import lazyfood.demo.models.Entity.Category;
import lazyfood.demo.models.DAO.CategoryDAO;

public class CategoryBO {
    private CategoryDAO categoryDAO;

    public CategoryBO() {
        categoryDAO = new CategoryDAO();
    }

    public ArrayList<Category> getAllCaterories() throws SQLException {
        return categoryDAO.getAllCategories();
    }

    public Category getCategoryById(String id) throws SQLException {
        return categoryDAO.getCategoryById(id);
    }

    public void addCategory(Category category) throws SQLException, IOException {
        if (categoryDAO.getCategoryById(category.getCategoryId()) == null)
            categoryDAO.addCategory(category);
        else
            throw new SQLIntegrityConstraintViolationException("Primary key is duplicated.");
    }

    public void updateCategory(Category category) throws SQLException, IOException {
        categoryDAO.updateCategory(category);
    }

    public void deleteCategory(String id) throws SQLException {
        if (categoryDAO.getCategoryById(id) != null)
            categoryDAO.deleteCategory(id);
        else
            throw new SQLException("Category not found");
    }
}
