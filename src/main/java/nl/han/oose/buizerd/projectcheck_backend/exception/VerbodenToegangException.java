package nl.han.oose.buizerd.projectcheck_backend.exception;

public class VerbodenToegangException extends IllegalArgumentException {

	public VerbodenToegangException(Long deelnemerId, String kamerCode) {
		super("Deelnemer met id {" + deelnemerId + "} in kamer met kamercode {" + kamerCode + "} heeft geen toegang");
	}

}
