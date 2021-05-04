package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 * Een kamer is een uitvoering van De ProjectCheck waar deelnemers zich aan bij kunnen
 * sluiten doormiddel van een unieke code.
 * <p>
 * Elke kamer wordt begeleid door een begeleider.
 */
@Entity
public class Kamer {

	// Wordt gebruikt voor het genereren van kamercodes
	private static final int KAMER_CODE_MAX = 999999;

	/**
	 * Genereert een unieke code.
	 *
	 * @return Een unieke code.
	 * @see java.util.concurrent.ThreadLocalRandom#nextInt(int)
	 */
	// package-private zodat het getest kan worden.
	static String genereerCode() {
		return String.valueOf(ThreadLocalRandom.current().nextInt(KAMER_CODE_MAX + 1));
	}

	/**
	 * De unieke code waarmee deelnemers kunnen deelnemen aan de kamer.
	 * Een unieke code kan niet aangepast worden.
	 */
	@Id
	@Column(updatable = false)
	private String kamerCode;

	/**
	 * De datum waarop de kamer is aangemaakt.
	 * Een datum kan niet aangepast worden.
	 */
	@Column(nullable = false, updatable = false)
	private LocalDateTime datum;

	/**
	 * De begeleider van de kamer.
	 */
	/*
	 * Het is niet nodig om dit bij te houden bij Spel in de datastore,
	 * maar het is wel nodig in de klasse Spel.
	 */
	@Transient
	private Begeleider begeleider;

	/**
	 * De deelnemers van de kamer.
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "kamer", orphanRemoval = true)
	private Set<Deelnemer> deelnemers;

	public Kamer() {
		this.kamerCode = Kamer.genereerCode();
		this.datum = LocalDateTime.now();
		this.deelnemers = new HashSet<>();
	}

	/**
	 * Haal de {@link Begeleider} van de kamer op.
	 *
	 * @return De begeleider van de kamer.
	 */
	public Begeleider getBegeleider() {
		return begeleider;
	}

	/**
	 * Zet de begeleider van de kamer.
	 *
	 * @param begeleider De {@link Begeleider} van de kamer.
	 */
	public void setBegeleider(@NotNull Begeleider begeleider) {
		this.begeleider = begeleider;
	}

	/**
	 * Haal de unieke code van de kamer op.
	 *
	 * @return De unieke code van de kamer.
	 */
	public String getKamerCode() {
		return kamerCode;
	}

	/**
	 * Haal het aantal deelnemers (incl. begeleider) van de kamer op.
	 *
	 * @return Het aantal deelnemers (incl. begeleider).
	 */
	int getAantalDeelnemers() {
		return (begeleider == null ? 0 : 1) + deelnemers.size();
	}

}
