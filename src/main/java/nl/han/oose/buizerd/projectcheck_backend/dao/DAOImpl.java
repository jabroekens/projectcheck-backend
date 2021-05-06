package nl.han.oose.buizerd.projectcheck_backend.dao;

import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Een generieke implementatie van {@link DAO}
 */
public class DAOImpl<T, K> implements DAO<T, K> {

	@PersistenceContext
	private EntityManager entityManager;

	@Resource
	private UserTransaction transaction;

	/**
	 * {@inheritDoc}
	 */
	public void create(T t) {
		doTransaction(em -> em.persist(t));
	}

	/**
	 * {@inheritDoc}
	 */
	public Optional<T> read(Class<T> klasseType, K k) {
		return Optional.ofNullable(entityManager.find(klasseType, k));
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(T t) {
		doTransaction(em -> em.merge(t));
	}

	/**
	 * {@inheritDoc}
	 */
	public void delete(K k) {
		doTransaction(em -> em.remove(k));
	}

	private void doTransaction(Consumer<EntityManager> consumer) throws RuntimeException {
		try {
			transaction.begin();
			consumer.accept(entityManager);
			transaction.commit();
		} catch (Exception e) {
			try {
				if (transaction.getStatus() == Status.STATUS_ACTIVE) {
					transaction.rollback();
					return;
				}
			} catch (SystemException se) {
				throw new RuntimeException(se);
			}

			throw new RuntimeException(e);
		}
	}

}
