package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.time.LocalDateTime;
import java.util.Collections;
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

	/**
	 * Construeert een {@link Kamer} met een automatisch gegenereerde unieke code.
	 */
	public Kamer() {
		this(Kamer.genereerCode(), LocalDateTime.now(), new HashSet<>());
	}

	/**
	 * Construeert een {@link Kamer} met een specifieke kamercode, datum, en set van deelnemers.
	 * <p>
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 */
	Kamer(@NotNull String kamerCode, @NotNull LocalDateTime datum, @NotNull Set<Deelnemer> deelnemers) {
		this.kamerCode = kamerCode;
		this.datum = datum;
		this.deelnemers = deelnemers;
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
	 * Haal de datum waarop de kamer is aangemaakt op.
	 *
	 * @return De datum waarop de kamer is aangemaakt.
	 */
	public LocalDateTime getDatum() {
		return datum;
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
	 * Haal een read-only kopie van de deelnemers van de kamer op.
	 *
	 * @return Een read-only kopie van de deelnemers van de kamer.
	 * @see java.util.Collections#unmodifiableSet(Set)
	 */
	public Set<Deelnemer> getDeelnemers() {
		return Collections.unmodifiableSet(deelnemers);
	}

}
