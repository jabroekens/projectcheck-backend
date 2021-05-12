package nl.han.oose.buizerd.projectcheck_backend.exception;

public class OngeldigeNaamException extends IllegalArgumentException {

	public OngeldigeNaamException(String naam) {
		super("Ongeldige naam: " + naam);
	}

}
