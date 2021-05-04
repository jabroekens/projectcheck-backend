package nl.han.oose.buizerd.projectcheck_backend.exception;

import javax.validation.constraints.NotNull;

/**
 * Geeft aan dat een kamer niet is gevonden.
 */
public class KamerNietGevondenException extends IllegalArgumentException {

	/**
	 * Construeert een {@link KamerNietGevondenException}.
	 *
	 * @param kamerCode De kamer code waarmee gezocht is.
	 */
	public KamerNietGevondenException(@NotNull String kamerCode) {
		super("Kamer met kamercode " + kamerCode + " is niet gevonden");
	}

}
