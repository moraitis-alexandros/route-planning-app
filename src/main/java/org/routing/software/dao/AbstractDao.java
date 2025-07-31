package org.routing.software.dao;

import jakarta.persistence.EntityManager;
import org.routing.software.jpos.AbstractEntity;
import org.routing.software.jpos.IdentifiableEntity;
import org.routing.software.service.JpaHelper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * This is a generic dao used for most class persistence
 * @param <T>
 */
public abstract class AbstractDao<T extends IdentifiableEntity> implements IGenericDAO<T> {

    private Class<T> persistenceClass;

    public AbstractDao() {}

    public Class<T> getPersistenceClass() {
        return persistenceClass;
    }

    public void setPersistenceClass(Class<T> persistenceClass) {
        this.persistenceClass = persistenceClass;
    }

    @Override
    public Optional<T> insert(T t) {
        EntityManager em = getEntityManager();
        em.persist(t);
        return Optional.of(t);
    }

    @Override
    public Optional<T> update(T t) {
        EntityManager em = getEntityManager();
        em.merge(t);
        return Optional.of(t);
    }

    @Override
    public void delete(Object id) {
        Optional<T> t = getById(id);
        if (Optional.of(t).isPresent()) {
            T entity = t.get();
            if (entity instanceof AbstractEntity) {
                ((AbstractEntity) entity).setActive(true);
                update(entity);
            } else {
                //TODO LOGGER
                throw new IllegalStateException("Entity does not extend AbstractEntity, cannot soft delete");
            }
        }
    }

    @Override
    public Optional<T> getById(Object id) {
        return Optional.ofNullable(getEntityManager().find(persistenceClass, id));
    }

    //TODO, we can use get by criteria with no critera
    @Override
    public List<T> getAll() {
        return List.of();
    }

    //TODO create the getByCriteria based on
    @Override
    public List<? extends T> getByCriteria(Map<String, Object> criteria) {
        return List.of();
    }

    @Override
    public List<T> getByCriteria(Class<T> clazz, Map<String, Object> criteria) {
        return List.of();
    }

    public EntityManager getEntityManager() {
        return JpaHelper.getEntityManager();
    }

}
