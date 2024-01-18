package lazyfood.demo.models.Entity;

import javax.persistence.*;

@Entity
@Table(name = "orderproduct")
public class ProductInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "OrderId")
    private Order _Order;

    @ManyToOne
    @JoinColumn(name = "ProductId")
    private Product _Product;

    @Column(name = "Quantity")
    private int Quantity;

    public ProductInOrder() {
        _Order = null;
        _Product = null;
        Quantity = 0;
    }

    public Order getOrder() {
        return _Order;
    }

    public void setOrder(Order order) {
        this._Order = order;
    }
    public Product getProduct() {
        return _Product;
    }

    public void setProduct(Product product) {
        this._Product = product;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
