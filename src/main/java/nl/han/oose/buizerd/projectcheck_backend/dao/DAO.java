package nl.han.oose.buizerd.projectcheck_backend.dao;

import java.io.Serializable;
import java.util.Optional;

public interface DAO {

	/**
	 * Slaat de entiteit {@code t} op.
	 *
	 * @param t de entiteit die opgeslagen moet worden
	 * @param <T> het type van de entiteit
	 */
	<T> void create(T t);

	/**
	 * Haalt een entiteit {@link T} op met de primaire sleutel {@code k}.
	 *
	 * @param klasseType de klasse van {@link T}
	 * @param k de primaire sleutel
	 * @param <T> het type van de entiteit
	 * @param <K> het type van de primaire sleutel
	 * @return een {@link Optional} met daarin een instantie van {@link T} met
	 *         de primaire sleutel {@code k} of een lege {@link Optional}
	 *         als er geen entiteit van het type {@link T} is gevonden
	 *         met de primaire sleutel {@code k}
	 */
	<T, K extends Serializable> Optional<T> read(Class<T> klasseType, K k);

	/**
	 * Updatet de opgeslagen staat van de entiteit {@code t}.
	 *
	 * @param t de entiteit die geüpdatet moet worden
	 * @param <T> het type van de entiteit
	 */
	<T> T update(T t);

	/**
	 * Verwijdert de entiteit {@code t}.
	 *
	 * @param t de entiteit die verwijdert moet worden
	 * @param <T> het type van de entiteit
	 */
	<T> void delete(T t);

}
