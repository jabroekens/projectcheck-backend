package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.executable.ValidateOnExecution;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;

/**
 * Een kamer is een uitvoering van De ProjectCheck waar deelnemers zich aan bij kunnen
 * sluiten doormiddel van een unieke code.
 * <p>
 * Elke kamer wordt begeleid door een begeleider.
 */
@Entity
public class Kamer {

	// Wordt gebruikt voor het genereren van kamercodes
	public static final int KAMER_CODE_MAX = 999999;

	/**
	 * Genereert een unieke code.
	 *
	 * @return Een unieke code.
	 * @see java.util.concurrent.ThreadLocalRandom#nextInt(int)
	 */
	@ValidateOnExecution
	public static @KamerCode
	String genereerCode() {
		return String.valueOf(ThreadLocalRandom.current().nextInt(Kamer.KAMER_CODE_MAX + 1));
	}

	/**
	 * De unieke code waarmee deelnemers kunnen deelnemen aan de kamer.
	 * Een unieke code kan niet aangepast worden.
	 */
	@KamerCode
	@Id
	@Column(nullable = false, updatable = false)
	private String kamerCode;

	/**
	 * De datum waarop de kamer is aangemaakt.
	 * Een datum kan niet aangepast worden.
	 */
	@NotNull
	@PastOrPresent
	@Column(nullable = false, updatable = false)
	private LocalDateTime datum;

	/**
	 * De begeleider van de kamer.
	 */
	/*
	 * Het is niet nodig om dit bij te houden bij Spel in de datastore,
	 * maar het is wel nodig in de klasse Spel.
	 */
	@NotNull
	@Valid
	@Transient
	private Begeleider begeleider;

	/**
	 * De deelnemers van de kamer.
	 */
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "kamer", orphanRemoval = true)
	private Set<@NotNull @Valid Deelnemer> deelnemers;

	/**
	 * Construeert een {@link Kamer}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	public Kamer() {
	}

	/**
	 * Construeert een {@link Kamer} onder begeleiding van een {@link Begeleider}.
	 *
	 * @param kamerCode De code van de kamer.
	 * @param begeleider De begeleider die de kamer begeleidt.
	 */
	@ValidateOnExecution
	public Kamer(@KamerCode String kamerCode, @NotNull @Valid Begeleider begeleider) {
		// Dit is een uitzondering op de comment bij `Kamer#Kamer(String, LocalDateTime, Begeleider, Set)`
		this(kamerCode, LocalDateTime.now(), begeleider, new HashSet<>());
	}

	/**
	 * Construeert een {@link Kamer} met een specifieke kamercode, datum, en set van deelnemers.
	 * <p>
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 *
	 * @param kamerCode De code van de kamer.
	 * @param datum De datum waarop de kamer gemaakt is.
	 * @param begeleider De begeleider van de kamer.
	 * @param deelnemers Een set van deelnemers van de kamer.
	 */
	Kamer(String kamerCode, LocalDateTime datum, Begeleider begeleider, Set<Deelnemer> deelnemers) {
		this.kamerCode = kamerCode;
		this.datum = datum;
		this.begeleider = begeleider;
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

	@ValidateOnExecution
	public Optional<Deelnemer> getDeelnemer(@Valid DeelnemerId deelnemerId) {
		return deelnemers.stream().filter(d -> d.getDeelnemerId().equals(deelnemerId)).findAny();
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
