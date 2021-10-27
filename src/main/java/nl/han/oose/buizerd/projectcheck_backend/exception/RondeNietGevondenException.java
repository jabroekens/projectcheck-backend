package nl.han.oose.buizerd.projectcheck_backend.exception;

public class RondeNietGevondenException extends IllegalStateException {

	public RondeNietGevondenException(String kamerCode) {
		super("Geen ronde gevonden voor kamer met kamercode {" + kamerCode + "}");
	}

}
