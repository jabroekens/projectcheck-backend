package nl.han.oose.buizerd.projectcheck_backend.exception;

public class DeelnemerHeeftGeenRolException extends IllegalStateException {

	public DeelnemerHeeftGeenRolException(Long deelnemerId, String kamerCode) {
		super("Deelnemer met deelnemer id {" + deelnemerId + "} in kamer met kamercode {" + kamerCode +
		      "} heeft geen rol");
	}

}
