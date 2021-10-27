package nl.han.oose.buizerd.projectcheck_backend.exception;

public class DeelnemerNietGevondenException extends IllegalArgumentException {

	public DeelnemerNietGevondenException(Long deelnemerId, String kamerCode) {
		super("Deelnemer met id {" + deelnemerId + "} is niet gevonden voor kamer met kamercode {" + kamerCode + "}");
	}

}
