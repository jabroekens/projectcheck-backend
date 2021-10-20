package nl.han.oose.buizerd.projectcheck_backend.exception;

import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;

public class DeelnemerNietGevondenException extends IllegalArgumentException {

	public DeelnemerNietGevondenException(DeelnemerId deelnemerId) {
		super("Deelnemer met id {" + deelnemerId.getId() + "} is niet gevonden voor kamer met kamercode {" + deelnemerId.getKamerCode() + "}");
	}

}
