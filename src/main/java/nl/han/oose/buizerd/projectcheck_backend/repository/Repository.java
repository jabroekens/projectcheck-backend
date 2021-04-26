package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
import javax.validation.constraints.NotNull;

/**
 * A generic repository interface following the Repository Pattern.
 *
 * @param <T> The domain class to be operated on.
 * @param <K> The identifier class of the domain class.
 */
public interface Repository<T, K> {

	/**
	 * Adds an instance of {@code T}.
	 *
	 * @param t The instance of {@link T} to add.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#create(Object)
	 */
	void add(@NotNull T t);

	/**
	 * Gets an instance of {@code T}.
	 *
	 * @param k The identifier of {@link T}.
	 * @return A nullable instance of {@code T} wrapped in {@link Optional}.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#read(Object)
	 */
	Optional<T> get(@NotNull K k);

	/**
	 * Updates an instances of {@code T}.
	 *
	 * @param t The instance of {@link T} to update.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#update(Object)
	 */
	void update(@NotNull T t);

	/**
	 * Removes an instance of {@code T}.
	 *
	 * @param k The identifier of {@link T}.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#delete(Object)
	 */
	void remove(@NotNull K k);

}
