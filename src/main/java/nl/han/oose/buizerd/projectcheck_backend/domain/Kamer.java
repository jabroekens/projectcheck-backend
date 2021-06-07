package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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

	@NotNull
	@Valid
	private KamerFase kamerFase;

	@Transient
	private Ronde huidigeRonde;

	/**
	 * De rollen die in de kamer ingeschakeld zijn.
	 */
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	private Set<@NotNull @Valid Rol> relevanteRollen;

	/**
	 * De deelnemers van de kamer.
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "kamer", orphanRemoval = true)
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
		// Dit is een uitzondering op de comment bij `Kamer#Kamer(String, LocalDateTime, KamerFase, Begeleider, Set, Set)`
		this(kamerCode, LocalDateTime.now(), KamerFase.SETUP, begeleider, new HashSet<>(), new HashSet<>());
	}

	/**
	 * Construeert een {@link Kamer} met een specifieke kamercode, datum, en set van deelnemers.
	 * <p>
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 *
	 * @param kamerCode De code van de kamer.
	 * @param datum De datum waarop de kamer gemaakt is.
	 * @param kamerFase De status van de kamer.
	 * @param begeleider De begeleider van de kamer.
	 * @param deelnemers Een set van deelnemers van de kamer.
	 * @param relevanteRollen Een set van ingeschakelde rollen van de kamer.
	 */
	Kamer(
		String kamerCode,
		LocalDateTime datum,
		KamerFase kamerFase,
		Begeleider begeleider,
		Set<Deelnemer> deelnemers,
		Set<Rol> relevanteRollen
	) {
		this.kamerCode = kamerCode;
		this.datum = datum;
		this.kamerFase = kamerFase;
		this.deelnemers = deelnemers;
		this.relevanteRollen = relevanteRollen;

		voegDeelnemerToe(begeleider);
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
	 * Haal de fase waarin de kamer zich verkeert op.
	 *
	 * @return De fase waarin de kamer zich verkeert.
	 */
	public KamerFase getKamerFase() {
		return kamerFase;
	}

	/**
	 * Zet de fase waarin de kamer zich verkeert.
	 *
	 * @param kamerFase De {@link KamerFase} waarin de kamer zich verkeert.
	 */
	@ValidateOnExecution
	public void setKamerFase(@NotNull @Valid KamerFase kamerFase) {
		this.kamerFase = kamerFase;
	}

	/**
	 * Haal de {@link Begeleider} van de kamer op.
	 *
	 * @return De begeleider van de kamer.
	 */
	public Begeleider getBegeleider() {
		Optional<Begeleider> begeleider = deelnemers.stream()
													.filter(Begeleider.class::isInstance)
													.map(Begeleider.class::cast)
													.findAny();

		if (begeleider.isEmpty()) {
			/*
			 * Gooi een IllegalStateException, omdat het niet voor hoort
			 * te komen dat er geen begeleider aanwezig is. Deze wordt
			 * namelijk normaliter in de constructor toegevoegd aan
			 * `deelnemers`.
			 */
			throw new IllegalStateException("De kamer heeft geen begeleider.");
		}

		return begeleider.get();
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

	/**
	 * Haal de deelnemer op met {@link DeelnemerId} {@code deelnemerId}.
	 */
	@ValidateOnExecution
	public Optional<Deelnemer> getDeelnemer(@NotNull @Valid DeelnemerId deelnemerId) {
		return deelnemers.stream().filter(d -> d.getDeelnemerId().equals(deelnemerId)).findAny();
	}

	/**
	 * Voegt een deelnemer toe aan de kamer en zet de kamer van de deelnemer.
	 * <p>
	 * Om een kip-en-eiprobleem te voorkomen is het nodig dat de kamer <em>achteraf</em>
	 * bij de {@link Deelnemer} gezet wordt. Zowel {@link Deelnemer} als {@link Kamer}
	 * zijn namelijk niet geldig zonder elkaar.
	 */
	@ValidateOnExecution
	public void voegDeelnemerToe(@NotNull @Valid Deelnemer deelnemer) {
		deelnemers.add(deelnemer);
		deelnemer.setKamer(this);
	}

	/**
	 * Genereert een ID dat gebruikt wordt in {@link DeelnemerId}.
	 *
	 * @return Het ID dat gebruikt wordt in {@link DeelnemerId}.
	 */
	public Long genereerDeelnemerId() {
		// Een begeleider heeft altijd het ID `1`, dus het ID van deelnemers begint vanaf `2`
		return deelnemers.size() + 1L;
	}

	/**
	 * Haal een read-only kopie op van de relevante rollen van de kamer.
	 *
	 * @return Een read-only kopie van de relevante rolen van de kamer.
	 * @see java.util.Collections#unmodifiableSet(Set)
	 */
	@ValidateOnExecution
	public Set<Rol> getRelevanteRollen() {
		return Collections.unmodifiableSet(relevanteRollen);
	}

	/**
	 * Schakel een relevante rol in voor de kamer.
	 *
	 * @param rollen De relevante rollen die ingeschakelt moet worden.
	 */
	@ValidateOnExecution
	public void activeerRelevanteRollen(@NotNull @Valid Set<Rol> rollen) {
		relevanteRollen = rollen;
	}

	public Ronde getHuidigeRonde() {
		return huidigeRonde;
	}

}
