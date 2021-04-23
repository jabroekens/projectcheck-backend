package nl.han.oose.buizerd.projectcheck_backend.dao;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.validation.constraints.NotNull;

public abstract class DAO<T> {

	private final Class<T> clazz;

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Constructs a {@code DAO}.
	 *
	 * @param clazz The class that the DAO represents.
	 */
	protected DAO(@NotNull Class<T> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Saves the state of {@code t}.
	 *
	 * @param t The instance to be saved.
	 * @see javax.persistence.EntityManager#persist(Object);
	 */
	public void create(T t) {
		doTransaction(entityManager -> entityManager.persist(t));
	}

	/**
	 * Retrieves an instantiated state of {@code T}.
	 *
	 * @param uuid The UUID of the state to retrieve.
	 * @return A nullable instance of {@code T} wrapped in {@link Optional}
	 * @see javax.persistence.EntityManager#find(Class, Object)
	 */
	public Optional<T> read(UUID uuid) {
		return Optional.ofNullable(entityManager.find(clazz, uuid));
	}

	/**
	 * Updates an existing state of {@code T}.
	 *
	 * @param t The instance to update.
	 * @see javax.persistence.EntityManager#merge(Object)
	 */
	public void update(T t) {
		doTransaction(entityManager -> entityManager.merge(t));
	}

	/**
	 * Deletes the saved state of the instance with {@code UUID} equal to {@uuid}.
	 *
	 * @param uuid The UUID of the saved state to delete.
	 * @see javax.persistence.EntityManager#remove(Object)
	 */
	public void delete(UUID uuid) {
		doTransaction(entityManager -> entityManager.remove(uuid));
	}

	/**
	 * Performs an operation in a transaction.
	 *
	 * @param consumer The operation to perform.
	 * @see javax.persistence.EntityManager#getTransaction()
	 */
	private void doTransaction(Consumer<EntityManager> consumer) throws IllegalStateException, RollbackException {
		EntityTransaction transaction = entityManager.getTransaction();

		try {
			transaction.begin();
			consumer.accept(entityManager);
			transaction.commit();
		} catch (IllegalStateException | RollbackException e) {
			transaction.rollback();
			throw (e);
		}
	}

}
