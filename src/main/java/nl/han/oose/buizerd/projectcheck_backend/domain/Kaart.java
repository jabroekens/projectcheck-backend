package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Een {@link Kaart} is een onderdeel van de ProjectCheck die kan worden
 * getoond op de frontend, met behulp van een custom text.
 */
@Entity
public class Kaart {

	/**
	 * De unieke id waarmee de kaart zal worden geïdentificeerd.
	 * De id wordt automatisch gegenereerd door de database.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private Integer id;

	/**
	 * De code van de kaart, hij is uniek binnen de kaartenset.
	 * TODO : Duidelijk hebben van de functie van de code.
	 */
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
	 *
	 * @param code De unieke code (per kaartenset) van de kaart.
	 * @param text De text die zich op de kaart zal plaatsvinden.
	 */
	public Kaart(Integer code, String text) {
		this.code = code;
		this.text = text;
	}

	/**
	 * Geeft de id (primary key) van de kaart terug.
	 *
	 * @return Het id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Geeft de unieke code van de kamer terug.
	 *
	 * @return De unieke code van de kamer.
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Geeft de text van de kaart terug.
	 *
	 * @return De text van de kaart.
	 */
	public String getText() {
		return text;
	}

}
