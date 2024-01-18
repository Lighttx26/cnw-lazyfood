package lazyfood.demo.models.DAO;

import java.util.List;

import lazyfood.demo.models.Entity.Product;
import org.hibernate.Session;


public class ProductDAO {
    public List<Product> getAllProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "SELECT p FROM Product p LEFT JOIN FETCH p.Category";
            return session.createQuery(query, Product.class).list();
        }
    }

    public Product getProductById(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "SELECT p FROM Product p LEFT JOIN FETCH p.Category WHERE p.ProductId = :id";
            return session.createQuery(query, Product.class).setParameter("id", id).uniqueResult();
        }
    }

    public void addProduct(Product product) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(product);
            session.getTransaction().commit();
        }
    }

    public void updateProduct(Product product) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.update(product);
            session.getTransaction().commit();
        }
    }

    public void deleteProduct(String id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Product product = session.get(Product.class, id);
            session.delete(product);
            session.getTransaction().commit();
        }
    }
}
