package lazyfood.demo.models.DAO;

import java.util.ArrayList;
import java.util.List;

import lazyfood.demo.models.Entity.Category;
import org.hibernate.Session;

public class CategoryDAO {
    public List<Category> getAllCategories() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Category", Category.class).list();
        }
    }

    public Category getCategoryById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Category.class, id);
        }
    }

    public void addCategory(Category category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(category);
            session.getTransaction().commit();
        }
    }

    public void updateCategory(Category category) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(category);
            session.getTransaction().commit();
        }
    }

    public void deleteCategory(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Category category = session.get(Category.class, id);
            session.delete(category);
            session.getTransaction().commit();
        }
    }
}
