package nl.han.oose.buizerd.projectcheck_backend.exceptions;

public class KamerNietGevondenExceptie extends IllegalArgumentException {

	public KamerNietGevondenExceptie(String kamerCode) {
		super(String.format("Kamer met kamercode {} is niet gevonden", kamerCode));
	}

}
