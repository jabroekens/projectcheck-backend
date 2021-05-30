package nl.han.oose.buizerd.projectcheck_backend.dao;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.UserTransaction;
import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Een generieke implementatie van {@link DAO}
 */
public class DAOImpl<T, K extends Serializable> implements DAO<T, K> {

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

	private void doTransaction(Consumer<EntityManager> consumer) {
		try {
			if (transaction.getStatus() != Status.STATUS_ACTIVE) {
				transaction.begin();
			}
			consumer.accept(entityManager);
			transaction.commit();
		} catch (NotSupportedException e) {
			consumer.accept(entityManager);
		} catch (HeuristicRollbackException | HeuristicMixedException | SystemException e) {
			// https://javaee.github.io/tutorial/ejb-basicexamples006.html
			throw new EJBException(e);
		} catch (RollbackException e) {
			try {
				if (transaction.getStatus() == Status.STATUS_ACTIVE) {
					transaction.rollback();
				}
			} catch (SystemException se) {
				throw new EJBException(se);
			}
		}
	}

}
