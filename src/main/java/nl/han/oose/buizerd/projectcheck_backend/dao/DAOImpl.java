package nl.han.oose.buizerd.projectcheck_backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

public class DAOImpl implements DAO {

	private EntityManager entityManager;

	@Transactional
	public <T> void create(T t) {
		entityManager.persist(t);
	}

	public <T, K extends Serializable> Optional<T> read(Class<T> klasseType, K k) {
		return Optional.ofNullable(entityManager.find(klasseType, k));
	}

	@Transactional
	public <T> T update(T t) {
		return entityManager.merge(t);
	}

	@Transactional
	public <T> void delete(T t) {
		entityManager.remove(t);
	}

	@PersistenceContext
	void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
