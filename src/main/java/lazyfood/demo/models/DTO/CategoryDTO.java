package lazyfood.demo.models.DTO;

import lazyfood.demo.models.Entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    public String CategoryId;
    public String CategoryName;
//    public List<ProductDTO> Products;

    public static CategoryDTO convertFromEntity(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.CategoryId = category.getCategoryId();
        categoryDTO.CategoryName = category.getCategoryName();
        return categoryDTO;
    }
}
