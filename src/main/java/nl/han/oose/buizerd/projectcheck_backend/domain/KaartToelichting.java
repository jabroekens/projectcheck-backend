package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

/**
 * De {@link KaartToelichting} is een onderdeel van de ProjectCheck
 * die hoort bij een {@link Kaart}.
 */
@Entity
public class KaartToelichting {

	/**
	 * De primary key van {@link KaartToelichting}.
	 * De id is auto incremented, en kan niet worden aangepast.
	 */
	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * De relevante {@link Kaart} waar de toelichting bij hoort.
	 * Het is notnull en kan niet worden aangepast.
	 */
	@Expose
	@OneToOne
	@Column(nullable = false, updatable = false)
	private Kaart kaart;

	/**
	 * De relevante toelichting voor de {@link Kaart}.
	 * Het is notnull en kan worden aangepast.
	 */
	@Expose
	@Column(nullable = false)
	private String toelichting;

	// TODO : Maak hier een abstracte klasse van.
	//private Set<String> feedback;

	/**
	 * Genereert een {@link KaartToelichting}.
	 * Deze constructor zal alleen gebruikt worden door JPA.
	 */
	public KaartToelichting() {
	}

	/**
	 * Genereert een {@link KaartToelichting}.
	 *
	 * @param kaart De relevante kaart voor de toelichting.
	 * @param toelichting De toelichting.
	 */
	public KaartToelichting(Kaart kaart, String toelichting) {
		this.kaart = kaart;
		this.toelichting = toelichting;
	}

	/**
	 * Geeft de unieke autoincrement id terug.
	 *
	 * @return De unieke id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Geeft de relevante kaart die bij de toelichting hoort terug.
	 *
	 * @return De relevante kaart.
	 */
	public Kaart getKaart() {
		return kaart;
	}

	/**
	 * Geeft de toelichting voor de relevante kaart terug.
	 *
	 * @return De toelichgting.
	 */
	public String getToelichting() {
		return toelichting;
	}

	/**
	 * Zet de toelichting voor de relevante kaart.
	 *
	 * @param toelichting De nieuwe toelichting.
	 */
	public void setToelichting(String toelichting) {
		this.toelichting = toelichting;
	}

}
