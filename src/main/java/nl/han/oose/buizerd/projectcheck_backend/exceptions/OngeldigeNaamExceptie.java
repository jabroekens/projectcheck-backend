package nl.han.oose.buizerd.projectcheck_backend.exceptions;

public class OngeldigeNaamExceptie extends IllegalArgumentException {

	public OngeldigeNaamExceptie(String gebruikersNaam) {
		super("Ongeldige naam: " + gebruikersNaam);
	}

}
