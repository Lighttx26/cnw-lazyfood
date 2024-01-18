package lazyfood.demo.models.DTO;

import lazyfood.demo.models.Entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDTO {
    public String CategoryId;
    public String CategoryName;
    public List<ProductDTO> Products = new ArrayList<>();
}
