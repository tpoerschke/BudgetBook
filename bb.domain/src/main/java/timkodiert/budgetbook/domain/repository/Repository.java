package timkodiert.budgetbook.domain.repository;

import java.util.Collection;
import java.util.List;
import javax.inject.Inject;

import timkodiert.budgetbook.domain.util.EntityManager;

public abstract class Repository<T> {

    @Inject
    protected EntityManager entityManager;
    protected final Class<T> entityType;

    public Repository(Class<T> entityType) {
        this.entityType = entityType;
    }

    public List<T> findAll() {
        return this.entityManager.findAll(entityType);
    }

    public void persist(T entity) {
        this.persist(List.of(entity));
    }

    public void persist(Collection<T> entities) {
        entities.forEach(this.entityManager::persist);
    }

    public void remove(T entity) {
        this.remove(List.of(entity));
    }

    public void remove(Collection<T> entities) {
        entities.forEach(this.entityManager::remove);
    }
}
