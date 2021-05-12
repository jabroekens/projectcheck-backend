package nl.han.oose.buizerd.projectcheck_backend.exception;

public class KamerNietGevondenException extends IllegalArgumentException {

	public KamerNietGevondenException(String kamerCode) {
		super("Kamer met kamercode {" + kamerCode + "} is niet gevonden");
	}

}
