package lazyfood.demo.models.DTO;

import lazyfood.demo.models.Entity.User;

public class UserDTO {
    public String UserId;
    public String UserName;
    public String Password;
    public String Role;
    public String FullName;
    public String Address;
    public String PhoneNumber;

    public static UserDTO convertFromEntity(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.UserId = user.getUserId();
        userDTO.UserName = user.getUsername();
        userDTO.Password = user.getPassword();
        userDTO.Role = user.getRole();
        userDTO.FullName = user.getFullname();
        userDTO.Address = user.getAddress();
        userDTO.PhoneNumber = user.getPhoneNumber();
        return userDTO;
    }
}
