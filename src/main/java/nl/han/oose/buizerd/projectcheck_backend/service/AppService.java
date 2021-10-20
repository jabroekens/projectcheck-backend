package nl.han.oose.buizerd.projectcheck_backend.service;

import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

public interface AppService {

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 */
	DeelnemerId maakKamer(@Naam String begeleiderNaam);

	/**
	 * Voegt een deelnemer met de naam {@code deelnemerNaam} toe aan de kamer met kamercode {@code kamerCode}
	 * als de kamer bestaat en open is.
	 */
	DeelnemerId neemDeel(@KamerCode String kamerCode, @Naam String deelnemerNaam)
	throws KamerNietGevondenException, KamerGeslotenException;

}
