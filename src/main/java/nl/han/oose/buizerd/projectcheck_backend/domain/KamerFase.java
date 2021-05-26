package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.util.NoSuchElementException;

/**
 * De huidige staat van een {@link Kamer}.
 */
public enum KamerFase {

	/**
	 * De kamer is aangemaakt, maar de relevante rollen moeten nog gekozen worden.
	 */
	SETUP,
	/**
	 * De relevante rollen zijn gekozen, deelnemers kunnen deelnemen aan de kamer.
	 */
	OPEN,
	/**
	 * Het spel is begonnen, nieuwe deelnemers kunnen zich niet bij een kamer toevoegen.
	 */
	BEZIG,
	/**
	 * Het spel is afgelopen, de kamer is gesloten.
	 */
	GESLOTEN;

	/**
	 * Geeft de volgende {@link KamerFase} terug.
	 *
	 * @return De volgende {@link KamerFase}.
	 * @throws NoSuchElementException Als er geen volgende {@link KamerFase} is.
	 */
	public KamerFase volgendeFase() {
		if (ordinal() == values().length - 1) {
			throw new NoSuchElementException();
		}

		return KamerFase.values()[this.ordinal() + 1];
	}

}
