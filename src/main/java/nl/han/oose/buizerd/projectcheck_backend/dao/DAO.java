package nl.han.oose.buizerd.projectcheck_backend.dao;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.io.Serializable;
import java.util.Optional;

/**
 * Een DAO volgens het DAO pattern.
 */
public interface DAO {

	/**
	 * Slaat de entiteit {@code t} op.
	 *
	 * @param t de entiteit die opgeslagen moet worden
	 * @param <T> het type van de entiteit
	 */
	@ValidateOnExecution
	<T> void create(@NotNull @Valid T t);

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
	@ValidateOnExecution
	<T, K extends Serializable> Optional<@Valid T> read(@NotNull Class<T> klasseType, @NotNull @Valid K k);

	/**
	 * Updatet de opgeslagen staat van de entiteit {@code t}.
	 *
	 * @param t de entiteit die geüpdatet moet worden
	 * @param <T> het type van de entiteit
	 */
	@ValidateOnExecution
	<T> T update(@NotNull @Valid T t);

	/**
	 * Verwijdert de entiteit {@code t}.
	 *
	 * @param t de entiteit die verwijdert moet worden
	 * @param <T> het type van de entiteit
	 */
	@ValidateOnExecution
	<T> void delete(@NotNull @Valid T t);

}
