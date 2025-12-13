package timkodiert.budgetbook.domain.repository;

import java.util.Collection;
import java.util.List;

import timkodiert.budgetbook.domain.util.EntityManager;

public abstract class Repository<T> {

    protected final EntityManager entityManager;
    protected final Class<T> entityType;

    protected Repository(EntityManager entityManager, Class<T> entityType) {
        this.entityManager = entityManager;
        this.entityType = entityType;
    }

    public List<T> findAll() {
        return this.entityManager.findAll(entityType);
    }

    public T findById(int id) {
        return this.entityManager.findById(entityType, id);
    }

    public void persist(T entity) {
        this.persist(List.of(entity));
    }

    public void persist(Collection<T> entities) {
        entities.forEach(this.entityManager::persist);
    }

    public T merge(T entity) {
        return this.entityManager.merge(entity);
    }

    public void merge(Collection<T> entities) {
        entities.forEach(this.entityManager::merge);
    }

    public void remove(T entity) {
        this.remove(List.of(entity));
    }

    public void remove(Collection<T> entities) {
        entities.forEach(this.entityManager::remove);
    }
}
