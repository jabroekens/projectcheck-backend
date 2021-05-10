package nl.han.oose.buizerd.projectcheck_backend.exceptions;

public class KamerNietGevondenExceptie extends IllegalArgumentException {

	public KamerNietGevondenExceptie(String kamerCode) {
		super("Kamer met kamercode {" + kamerCode + "} is niet gevonden");
	}

}
