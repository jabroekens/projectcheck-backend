package nl.han.oose.buizerd.projectcheck_backend.dao;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;
import java.io.Serializable;
import java.util.Optional;

/**
 * Een generieke implementatie van {@link DAO}
 */
public class DAOImpl implements DAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction transaction;

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
	 * @param transaction Een {@link UserTransaction}.
	 */
	public DAOImpl(EntityManager entityManager, UserTransaction transaction) {
		this.entityManager = entityManager;
		this.transaction = transaction;
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
	public <T> void update(T t) {
		entityManager.merge(t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional
	public <T> void delete(T t) {
		entityManager.remove(t);
	}

}
