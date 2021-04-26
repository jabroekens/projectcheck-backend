package nl.han.oose.buizerd.projectcheck_backend.dao;

import java.util.Optional;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import javax.validation.constraints.NotNull;

/**
 * A generic DAO following the DAO Pattern.
 *
 * @param <T> The domain class to be operated on.
 * @param <K> The identifier class of the domain class.
 */
public abstract class DAO<T, K> {

	static final EntityManagerFactory emf;

	static {
		emf = Persistence.createEntityManagerFactory("SQLServer");
	}

	private final Class<T> type;

	/**
	 * Constructs a {@code DAO}.
	 *
	 * @param type The class that the DAO represents.
	 */
	protected DAO(@NotNull Class<T> type) {
		this.type = type;
	}

	/**
	 * Saves the state of {@code t}.
	 *
	 * @param t The instance to be saved.
	 * @see javax.persistence.EntityManager#persist(Object)
	 */
	public void create(@NotNull T t) {
		doTransaction(em -> em.persist(t));
	}

	/**
	 * Retrieves an instantiated state of {@code T}.
	 *
	 * @param k The identifier of the state to retrieve.
	 * @return A nullable instance of {@code T} wrapped in {@link Optional}.
	 * @see javax.persistence.EntityManager#find(Class, Object)
	 */
	public Optional<T> read(@NotNull K k) {
		EntityManager em = emf.createEntityManager();
		return Optional.ofNullable(em.find(type, k));
	}

	/**
	 * Updates an existing state of {@code T}.
	 *
	 * @param t The instance to update.
	 * @see javax.persistence.EntityManager#merge(Object)
	 */
	public void update(@NotNull T t) {
		doTransaction(em -> em.merge(t));
	}

	/**
	 * Deletes the saved state of the instance with {@code K} equal to {@code k}.
	 *
	 * @param k The identifier of the saved state to delete.
	 * @see javax.persistence.EntityManager#remove(Object)
	 */
	public void delete(@NotNull K k) {
		doTransaction(em -> em.remove(k));
	}

	/**
	 * Performs an operation in a transaction.
	 *
	 * @param consumer The operation to perform.
	 * @see javax.persistence.EntityManager#getTransaction()
	 */
	protected void doTransaction(@NotNull Consumer<EntityManager> consumer) throws IllegalStateException, RollbackException {
		EntityManager em = emf.createEntityManager();
		EntityTransaction transaction = em.getTransaction();

		try {
			transaction.begin();
			consumer.accept(em);
			transaction.commit();
		} catch (IllegalStateException | RollbackException e) {
			transaction.rollback();
			throw (e);
		}
	}

}
