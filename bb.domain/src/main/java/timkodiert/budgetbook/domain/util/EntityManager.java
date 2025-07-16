package timkodiert.budgetbook.domain.util;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

import jakarta.inject.Named;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

@Singleton
public class EntityManager {

    @Getter
    private final Session session;

    @Inject
    public EntityManager(@Named("dbPath") String dbPath) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .applySetting("hibernate.connection.url", dbPath)
                .build();

        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        this.session = sessionFactory.openSession();
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
