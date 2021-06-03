package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Een {@link Kaart} is een onderdeel van de ProjectCheck die kan worden
 * getoond op de frontend, met behulp van een custom text.
 */
@Entity
public class Kaart {

	/**
	 * De unieke code waarmee de kaart zal worden geïdentificeerd.
	 * Deze unieke code kan niet worden aangepast.
	 */
	@Id
	@Column(nullable = false, updatable = false)
	private Integer code;

	/**
	 * De text die op de kaart zal plaatsvinden.
	 * Deze text zal niet aangepast kunnen worden.
	 */
	@Column(nullable = false, updatable = false)
	private String text;

	/**
	 * Genereert een {@link Kaart}.
	 * Deze constructor zal alleen gebruikt worden door JPA.
	 */
	public Kaart() {
	}

	/**
	 * Genereert een {@link Kaart}.
	 * @param code De unieke code van de kaart.
	 * @param text De text die zich op de kaart zal plaatsvinden.
	 */
	public Kaart(int code, String text) {
		this.code = code;
		this.text = text;
	}

	/**
	 * Geeft de unieke code van de kamer terug.
	 * @return De unieke code van de kamer.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Geeft de text van de kaart terug.
	 * @return De text van de kaart.
	 */
	public String getText() {
		return text;
	}
}
