package lazyfood.demo.models.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lazyfood.demo.models.Entity.User;

public class UserDAO {

    public User getUserById(String id) throws SQLException {
        User user = null;
        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT * FROM user where UserId = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, id);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at User.getUserById()" + e.getMessage());
        }

        return user;
    }

    public User getUserByUsername(String username) throws SQLException {
        User user = null;
        try (Connection conn = DBConnector.getConnection()) {
            String query = "SELECT * FROM user where Username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
                        rs.getString(6), rs.getString(7));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at User.getUserById()" + e.getMessage());
        }

        return user;
    }

    public void addUser(User user) throws SQLException {
        try (Connection conn = DBConnector.getConnection()) {
            String query = "INSERT INTO user VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, user.getUserId());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getFullname());
            statement.setString(6, user.getPhoneNumber());
            statement.setString(7, user.getAddress());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Exception at User.addUser()" + e.getMessage());
        }
    }
}
