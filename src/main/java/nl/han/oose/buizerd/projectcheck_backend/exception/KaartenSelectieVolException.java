package nl.han.oose.buizerd.projectcheck_backend.exception;

public class KaartenSelectieVolException extends IllegalStateException {

	public KaartenSelectieVolException(Long deelnemerId, String kamerCode) {
		super("De kaartenselectie voor deelnemer met id {" + deelnemerId + "} in kamer met kamercode {" + kamerCode +
		      "} is vol");
	}

}
