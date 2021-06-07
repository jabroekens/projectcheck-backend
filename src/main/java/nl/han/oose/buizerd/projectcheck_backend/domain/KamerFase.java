package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.util.NoSuchElementException;

/**
 * De huidige staat van een {@link Kamer}.
 */
public enum KamerFase {

	/**
	 * De kamer is aangemaakt maar de relevante rollen moeten nog gekozen worden.
	 */
	SETUP,
	/**
	 * De relevante rollen zijn gekozen. Deelnemers kunnen deelnemen aan de kamer.
	 */
	OPEN,
	/**
	 * Het spel is begonnen. Nieuwe deelnemers kunnen zich niet meer bij een kamer voegen.
	 */
	BEZIG,
	/**
	 * Het spel is afgelopen. De kamer is gesloten.
	 */
	GESLOTEN;

	/**
	 * @return de volgende {@link KamerFase}
	 */
	public KamerFase getVolgendeFase() {
		if (ordinal() == values().length - 1) {
			throw new NoSuchElementException();
		}

		return values()[ordinal() + 1];
	}

}
