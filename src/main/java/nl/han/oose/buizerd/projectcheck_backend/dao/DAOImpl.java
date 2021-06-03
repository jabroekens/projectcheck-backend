package nl.han.oose.buizerd.projectcheck_backend.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.util.Optional;

/**
 * Een generieke implementatie van {@link DAO}
 */
public class DAOImpl implements DAO {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Construeert een {@link DAOImpl}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door Jakarta CDI en mag niet aangeroepen worden.</b>
	 */
	public DAOImpl() {
	}

	/**
	 * Construeert een {@link DAOImpl}.
	 * <p>
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 *
	 * @param entityManager Een {@link EntityManager}.
	 */
	public DAOImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public <T> void create(T t) {
		entityManager.persist(t);
	}

	/**
	 * {@inheritDoc}
	 */
	public <T, K extends Serializable> Optional<T> read(Class<T> klasseType, K k) {
		return Optional.ofNullable(entityManager.find(klasseType, k));
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public <T> T update(T t) {
		return entityManager.merge(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public <T> void delete(T t) {
		entityManager.remove(t);
	}

}
