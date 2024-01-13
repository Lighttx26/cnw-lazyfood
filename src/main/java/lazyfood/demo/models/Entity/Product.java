package lazyfood.demo.models.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "ProductId")
    private String ProductId;
    @Column(name = "ProductName")

    private String ProductName;
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category Category;
    @Column(name = "Price")

    private double Price;
    @Column(name = "IsAvailable")

    private boolean IsAvailable;
    @Column(name = "Image")

    private String Image;

//    @OneToMany(mappedBy = "product")
//    private List<ProductInOrder> ProductsInOrder;

    public Product() {
        this.ProductId = "";
        this.ProductName = "";
        this.Category = null;
        this.Price = 0;
        this.IsAvailable = true;
        this.Image = "";
    }

    // From database
//    public Product(String productId, String productName, String categoryId, String categoryName, double price,
//            boolean isAvailable, String image) {
//        this.ProductId = productId;
//        this.ProductName = productName;
//        this.CategoryId = categoryId;
//        this.CategoryName = categoryName;
//        this.Price = price;
//        this.IsAvailable = isAvailable;
//        this.Image = image;
//    }

    // From input
    public Product(String productId, String productName, Category category, double price, String image) {
        this.ProductId = productId;
        this.ProductName = productName;
        this.Category = category;
        this.Price = price;
        this.IsAvailable = true;
        this.Image = image;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public Category getCategory() {
        return Category;
    }

    public void setCategory(Category category) {
        Category = category;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public boolean isAvailable() {
        return IsAvailable;
    }

    public void setAvailable(boolean available) {
        IsAvailable = available;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        this.Image = image;
    }
}
