package timkodiert.budgetBook.util;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import jakarta.persistence.criteria.CriteriaQuery;

public class EntityManager {

    private static EntityManager INSTANCE;

    private Session session;

    private EntityManager() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .applySetting("hibernate.connection.url",
                        PropertiesService.getInstance().getProperties().getProperty("db"))
                .build();

        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        this.session = sessionFactory.openSession();
    }

    public static EntityManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EntityManager();
        }
        return INSTANCE;
    }

    public void closeSession() {
        this.session.close();
    }

    public <T> CriteriaQuery<T> criteriaQuery(Class<T> entityClass) {
        CriteriaQuery<T> criteriaQuery = this.session.getCriteriaBuilder().createQuery(entityClass);
        criteriaQuery.from(entityClass);
        return criteriaQuery;
    }

    public <T> List<T> findAll(Class<T> entityClass) {
        CriteriaQuery<T> criteriaQuery = this.session.getCriteriaBuilder().createQuery(entityClass);
        criteriaQuery.from(entityClass);
        return this.session.createQuery(criteriaQuery).list();
    }

    public void persist(Object... objects) {
        this.session.beginTransaction();
        Arrays.stream(objects).forEach(this.session::persist);
        this.session.getTransaction().commit();
    }

    public void remove(Object... objects) {
        this.session.beginTransaction();
        Arrays.stream(objects).forEach(this.session::remove);
        this.session.getTransaction().commit();
    }

    public void refresh(Object object) {
        this.session.refresh(object);
    }
}
