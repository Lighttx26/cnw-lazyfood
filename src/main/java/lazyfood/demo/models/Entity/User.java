package lazyfood.demo.models.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    private String UserId;
    @Column(name = "Username", unique = true)
    private String Username;

    @Column(name = "Password")
    private String Password;

    @Column(name = "Role")
    private String Role;

    @Column(name = "Fullname")
    private String Fullname;

    @Column(name = "PhoneNumber")
    private String PhoneNumber;

    @Column(name = "Address")
    private String Address;

    public User() {
        UserId = "";
        Username = "";
        Password = "";
        Role = "";
        Fullname = "";
        PhoneNumber = "";
        Address = "";
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
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
}
