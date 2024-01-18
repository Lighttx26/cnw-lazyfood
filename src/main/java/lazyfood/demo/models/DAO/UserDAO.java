package lazyfood.demo.models.DAO;

import lazyfood.demo.models.Entity.User;
import org.hibernate.Session;

import javax.persistence.Query;

public class UserDAO {
    public User getUserById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(User.class, id);
        }
    }

    public User getUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query query = session.createQuery("from User where Username = :username");
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        }
    }

    public void addUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        }
    }
}
