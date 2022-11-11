package timkodiert.budgetBook.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class EntityManager {

    private static EntityManager INSTANCE;

    private Session session;

    private EntityManager() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        SessionFactory sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        this.session = sessionFactory.openSession();
    }

    public static EntityManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new EntityManager();
        }
        return INSTANCE;
    }

    public void closeSession() {
        this.session.close();
    }

    public void persist(Object object) {
        // Im Kontext dieser Applikation wird stets nur
        // eine Entity (oder wenige) gleichzeitig persistiert
        this.session.beginTransaction();
        this.session.persist(object);
        this.session.getTransaction().commit();
    }
}
