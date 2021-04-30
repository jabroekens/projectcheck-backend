package nl.han.oose.buizerd.projectcheck_backend.exceptions;

public class InvalideGebruikerException extends IllegalArgumentException {

	public InvalideGebruikerException(String gebruikersNaam) {
		super(String.format("Ongeldige gebruikersnaam: ", gebruikersNaam));
	}

}
