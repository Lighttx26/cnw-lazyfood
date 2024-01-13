package lazyfood.demo.models.BO;

import java.sql.SQLException;

import lazyfood.demo.models.Entity.User;
import lazyfood.demo.models.DAO.UserDAO;

class AuthenticationStatus {
    public static final int USERNAME_NOT_FOUND = 0;
    public static final int WRONG_PASSWORD = 1;
    public static final int LOGIN_SUCCESSFULLY = 2;
}

public class LoginBO {
    private UserDAO userDAO;

    public LoginBO() {
        userDAO = new UserDAO();
    }

    public int Authenticate(String username, String password) throws SQLException {
        User user = userDAO.getUserByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password))
                return AuthenticationStatus.LOGIN_SUCCESSFULLY;
            else
                return AuthenticationStatus.WRONG_PASSWORD;
        } else
            return AuthenticationStatus.USERNAME_NOT_FOUND;
    }
}
