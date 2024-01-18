package lazyfood.demo.models.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`")
public class Order {
    @Id
    @Column(name = "OrderId")
    private String OrderId;
    @ManyToOne
    @JoinColumn(name = "CustomerId")
    private User Customer;

    @OneToMany(mappedBy = "_Order", cascade = CascadeType.ALL)
    private List<ProductInOrder> Products;

    @Column(name = "Time")
    private LocalDateTime Time;
    @Column(name = "PhoneNumber")
    private String PhoneNumber;
    @Column(name = "Address")
    private String Address;
    @Column(name = "IsDelivered")
    private boolean IsDelivered;

    public Order() {
        this.OrderId = "";
        this.Customer = null;
        this.Products = new ArrayList<ProductInOrder>();
        this.Time = null;
        this.PhoneNumber = "";
        this.Address = "";
        this.IsDelivered = false;
    }

    public String getOrderId() {
        return OrderId;
    }

     public void setOrderId(String orderId) {
        OrderId = orderId;
     }

    public User getCustomer() {
        return Customer;
    }

     public void setCustomer(User customer) {
         Customer = customer;
     }

    public List<ProductInOrder> getProducts() {
        return Products;
    }

    public void setProducts(List<ProductInOrder> products) {
        Products = products;
    }

    public LocalDateTime getTime() {
        return Time;
    }

    public void setTime(LocalDateTime time) {
        Time = time;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public boolean isDelivered() {
        return IsDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        IsDelivered = isDelivered;
    }
}
