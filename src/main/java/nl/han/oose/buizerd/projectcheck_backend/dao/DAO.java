package nl.han.oose.buizerd.projectcheck_backend.dao;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.executable.ValidateOnExecution;

/**
 * Een DAO volgens het DAO pattern.
 *
 * @param <T> De domeinklasse waarmee gewerkt moet worden.
 * @param <K> De identifier van de domeinklasse.
 */
public interface DAO<T, K> {

	/**
	 * Sla de staat van {@code t} op.
	 *
	 * @param t De instantie die opgeslagen moet worden.
	 * @see javax.persistence.EntityManager#persist(Object)
	 */
	@ValidateOnExecution
	void create(@NotNull @Valid T t);

	/**
	 * Haal een instantie van {@code T} op met de identifier {@code k}.
	 *
	 * @param klasseType Het klassetype van {@link T}.
	 * @param k De identifier van de op te halen instantie.
	 * @return Een nullable instantie van {@link T} gewikkelt in {@link Optional}.
	 * @see javax.persistence.EntityManager#find(Class, Object)
	 */
	@ValidateOnExecution
	Optional<T> read(@NotNull Class<T> klasseType, @NotNull @Valid K k);

	/**
	 * Update de opgeslagen staat van {@code t}.
	 *
	 * @param t De instantie waarmee de opgeslagen instantie van {@link T} vervangen moet worden.
	 * @see javax.persistence.EntityManager#merge(Object)
	 */
	@ValidateOnExecution
	void update(@NotNull @Valid T t);

	/**
	 * Verwijder de opgeslagen staat van een instantie van {@code T} met de identifier {@code k}.
	 *
	 * @param k De identifier van de opgeslagen staat die verwijderd moet worden.
	 * @see javax.persistence.EntityManager#remove(Object)
	 */
	@ValidateOnExecution
	void delete(@NotNull @Valid K k);

}
