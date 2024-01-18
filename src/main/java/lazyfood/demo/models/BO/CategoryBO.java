package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import lazyfood.demo.models.DTO.CategoryDTO;
import lazyfood.demo.models.Entity.Category;
import lazyfood.demo.models.DAO.CategoryDAO;

public class CategoryBO {
    private final CategoryDAO categoryDAO;

    public CategoryBO() {
        categoryDAO = new CategoryDAO();
    }

    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();

        List<Category> categoryEntityList = categoryDAO.getAllCategories();
        for (Category category : categoryEntityList) {
            CategoryDTO categoryDTO = CategoryDTO.convertFromEntity(category);
            categoryDTOList.add(categoryDTO);
        }

        return categoryDTOList;
    }

    public CategoryDTO getCategoryById(String id) {
        Category category = categoryDAO.getCategoryById(id);
        return CategoryDTO.convertFromEntity(category);
    }

    public void addCategory(CategoryDTO categoryDTO) throws SQLException {
        if (categoryDAO.getCategoryById(categoryDTO.CategoryId) == null) {
            Category category = ConvertToEntity(categoryDTO);
            categoryDAO.addCategory(category);
        }
        else
            throw new SQLIntegrityConstraintViolationException("Primary key is duplicated.");
    }

    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = ConvertToEntity(categoryDTO);
        categoryDAO.updateCategory(category);
    }

    public void deleteCategory(String id) throws SQLException {
        if (categoryDAO.getCategoryById(id) != null)
            categoryDAO.deleteCategory(id);
        else
            throw new SQLException("Category not found");
    }

    private Category ConvertToEntity(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setCategoryId(categoryDTO.CategoryId);
        category.setCategoryName(categoryDTO.CategoryName);
        return category;
    }
}
