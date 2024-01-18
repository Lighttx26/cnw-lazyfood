package lazyfood.demo.models.DAO;

import lazyfood.demo.models.Entity.Order;

import java.util.List;
import lazyfood.demo.models.Entity.ProductInOrder;
import org.hibernate.Session;

public class OrderDAO {
    public List<Order> getAllOrders() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Order", Order.class).list();
        }
    }

    public List<Order> getOrdersByUser(String userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "from Order where Customer.UserId = :userId";
            return session.createQuery(query, Order.class).setParameter("userId", userId).list();
        }
    }

    public Order getOrderById(String orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String query = "SELECT o FROM Order o LEFT JOIN FETCH o.Products WHERE o.OrderId = :orderId";
            return session.createQuery(query, Order.class).setParameter("orderId", orderId).uniqueResult();
        }
    }

    public void createOrder(Order order) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            session.save(order);

            for (ProductInOrder product : order.getProducts()) {
                session.save(product);
            }
            session.getTransaction().commit();
        }

    }

    public void setDeliveredState(String orderId, boolean state) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Order order = session.get(Order.class, orderId);
            order.setIsDelivered(state);
            session.update(order);
            session.getTransaction().commit();
        }
    }

    public void deleteOrder(String orderId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();
            Order order = session.get(Order.class, orderId);
            session.delete(order);
            session.getTransaction().commit();
        }
    }
}