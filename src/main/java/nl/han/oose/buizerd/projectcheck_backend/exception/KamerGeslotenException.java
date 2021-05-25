package nl.han.oose.buizerd.projectcheck_backend.exception;

public class KamerGeslotenException extends IllegalStateException {

	public KamerGeslotenException(String kamerCode) {
		super("Kamer met kamercode {" + kamerCode + "} is gesloten");
	}

}
