package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import lazyfood.demo.models.DTO.UserDTO;
import lazyfood.demo.models.Entity.User;
import lazyfood.demo.models.DAO.UserDAO;

public class UserBO {
    private final UserDAO userDAO;

    public UserBO() {
        userDAO = new UserDAO();
    }

    public UserDTO getUserById(String id) {
        User user = userDAO.getUserById(id);
        return UserDTO.convertFromEntity(user);
    }

    public UserDTO getUserByUsername(String username) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        return UserDTO.convertFromEntity(user);
    }

    public void addUser(UserDTO userDTO) throws SQLException {
        if (userDAO.getUserByUsername(userDTO.UserName) != null)
            throw new SQLIntegrityConstraintViolationException("Username already exists");
        User user = convertToEntity(userDTO);
        userDAO.addUser(user);
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setUserId(userDTO.UserId);
        user.setUsername(userDTO.UserName);
        user.setRole(userDTO.Role);
        user.setPassword(userDTO.Password);
        user.setFullname(userDTO.FullName);
        user.setAddress(userDTO.Address);
        user.setPhoneNumber(userDTO.PhoneNumber);
        return user;
    }
}
