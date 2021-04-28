package nl.han.oose.buizerd.projectcheck_backend.dao;

import java.util.Optional;
import javax.validation.constraints.NotNull;

/**
 * A generic DAO following the DAO Pattern.
 *
 * @param <T> The domain class to be operated on.
 * @param <K> The identifier class of the domain class.
 */
public interface DAO<T, K> {

	/**
	 * Saves the state of {@code t}.
	 *
	 * @param t The instance to be saved.
	 * @see javax.persistence.EntityManager#persist(Object)
	 */
	void create(@NotNull T t);

	/**
	 * Retrieves an instantiated state of {@code T}.
	 *
	 * @param k The identifier of the state to retrieve.
	 * @return A nullable instance of {@code T} wrapped in {@link Optional}.
	 * @see javax.persistence.EntityManager#find(Class, Object)
	 */
	Optional<T> read(@NotNull Class<T> entityClass, @NotNull K k);

	/**
	 * Updates an existing state of {@code T}.
	 *
	 * @param t The instance to update.
	 * @see javax.persistence.EntityManager#merge(Object)
	 */
	void update(@NotNull T t);

	/**
	 * Deletes the saved state of the instance with {@code K} equal to {@code k}.
	 *
	 * @param k The identifier of the saved state to delete.
	 * @see javax.persistence.EntityManager#remove(Object)
	 */
	void delete(@NotNull K k);

}
