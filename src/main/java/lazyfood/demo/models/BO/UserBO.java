package lazyfood.demo.models.BO;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import lazyfood.demo.models.Entity.User;
import lazyfood.demo.models.DAO.UserDAO;

public class UserBO {
    private final UserDAO userDAO;

    public UserBO() {
        userDAO = new UserDAO();
    }

    public User getUserById(String id) {
        return userDAO.getUserById(id);
    }

    public User getUserByUsername(String username) throws SQLException {
        return userDAO.getUserByUsername(username);
    }

    public void addUser(User user) throws SQLException {
        if (userDAO.getUserByUsername(user.getUsername()) != null)
            throw new SQLIntegrityConstraintViolationException("Username already exists");
        userDAO.addUser(user);
    }
}
