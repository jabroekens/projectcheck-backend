package nl.han.oose.buizerd.projectcheck_backend.dao;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.io.Serializable;
import java.util.Optional;

/**
 * Een DAO volgens het DAO pattern.
 *
 * @param <T> De domeinklasse waarmee gewerkt moet worden.
 * @param <K> De identifier van de domeinklasse.
 */
public interface DAO<T, K extends Serializable> {

	/**
	 * Sla de staat van {@code t} op.
	 *
	 * @param t De instantie die opgeslagen moet worden.
	 * @see jakarta.persistence.EntityManager#persist(Object)
	 */
	@ValidateOnExecution
	void create(@NotNull @Valid T t);

	/**
	 * Haal een instantie van {@code T} op met de identifier {@code k}.
	 *
	 * @param klasseType Het klassetype van {@link T}.
	 * @param k De identifier van de op te halen instantie.
	 * @return Een nullable instantie van {@link T} gewikkelt in {@link Optional}.
	 * @see jakarta.persistence.EntityManager#find(Class, Object)
	 */
	@ValidateOnExecution
	Optional<@Valid T> read(@NotNull Class<T> klasseType, @NotNull @Valid K k);

	/**
	 * Update de opgeslagen staat van {@code t}.
	 *
	 * @param t De instantie waarmee de opgeslagen instantie van {@link T} vervangen moet worden.
	 * @see jakarta.persistence.EntityManager#merge(Object)
	 */
	@ValidateOnExecution
	void update(@NotNull @Valid T t);

	/**
	 * Verwijder de opgeslagen staat van een instantie van {@code T} met de identifier {@code k}.
	 *
	 * @param k De identifier van de opgeslagen staat die verwijderd moet worden.
	 * @see jakarta.persistence.EntityManager#remove(Object)
	 */
	@ValidateOnExecution
	void delete(@NotNull @Valid K k);

}
