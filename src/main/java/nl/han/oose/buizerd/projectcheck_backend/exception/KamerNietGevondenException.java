package nl.han.oose.buizerd.projectcheck_backend.exception;

import javax.validation.constraints.NotNull;

public class KamerNietGevondenException extends IllegalArgumentException {

	public KamerNietGevondenException(@NotNull String kamerCode) {
		super("Kamer met kamercode " + kamerCode + " is niet gevonden");
	}

}
