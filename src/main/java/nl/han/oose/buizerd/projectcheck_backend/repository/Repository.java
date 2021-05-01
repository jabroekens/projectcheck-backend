package nl.han.oose.buizerd.projectcheck_backend.repository;

import java.util.Optional;
import javax.validation.constraints.NotNull;

/**
 * Een Repository volgens het Repository pattern.
 *
 * @param <T> De domeinklasse waarmee gewerkt moet worden.
 * @param <K> De identifier van de domeinklasse.
 */
public interface Repository<T, K> {

	/**
	 * Voeg een instantie van {@code T} toe.
	 *
	 * @param t De instantie die toegevoegd moet worden.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#create(Object)
	 */
	void add(@NotNull T t);

	/**
	 * Haal een instantie van {@code T} op met de identifier {@code k}.
	 *
	 * @param k De identifier van de op te halen instantie.
	 * @return Een nullable instantie van {@link T} gewikkelt in {@link Optional<T>}.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#read(Class, Object)
	 */
	Optional<T> get(@NotNull K k);

	/**
	 * Update de toegevoegde instantie van {@code T}.
	 *
	 * @param t De instantie waarmee de toegevoegde instantie van {@link T} vervangen moet worden.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#update(Object)
	 */
	void update(@NotNull T t);

	/**
	 * Verwijder de toegevoegde instantie van {@code T} met de identifier {@code k}.
	 *
	 * @param k De identifier van de toegevoegde instantie die verwijderd moet worden.
	 * @see nl.han.oose.buizerd.projectcheck_backend.dao.DAO#delete(Object)
	 */
	void remove(@NotNull K k);

}
