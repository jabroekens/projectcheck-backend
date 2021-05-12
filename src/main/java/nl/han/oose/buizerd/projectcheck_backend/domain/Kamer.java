package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import nl.han.oose.buizerd.projectcheck_backend.exception.RoleNotFoundException;
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
	 * De rollen die in de kamer ingeschakeld zijn.
	 */
	@ElementCollection
	private Set<Rol> relevanteRollen;

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
		this(kamerCode, LocalDateTime.now(), begeleider, new HashSet<>(), EnumSet.noneOf(Rol.class));
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
	Kamer(String kamerCode, LocalDateTime datum, Begeleider begeleider, Set<Deelnemer> deelnemers, Set<Rol> relevanteRollen) {
		this.kamerCode = kamerCode;
		this.datum = datum;
		this.begeleider = begeleider;
		this.deelnemers = deelnemers;
		this.relevanteRollen = relevanteRollen;
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

	/**
	 * Voeg een deelnemer toe aan de kamer.
	 *
	 * @param deelnemer De {@link Deelnemer} die toegevoegd moet worden.
	 */
	@ValidateOnExecution
	public void voegDeelnemerToe(@NotNull @Valid Deelnemer deelnemer) {
		deelnemers.add(deelnemer);

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

	/*
	 * Haal een read-only kopie op van de relevante rollen van de kamer.
	 *
	 * @return Een read-only kopie van de relevante rolen van de kamer.
	 */
	@ValidateOnExecution
	public Set<Rol> getRelevanteRollen() {
		return relevanteRollen;
	}

	@ValidateOnExecution
	public Optional<Rol> getRelevanteRol(@Valid String rolNaam) {
		return relevanteRollen.stream().filter(r -> r.getRolNaam().equals(rolNaam)).findAny();
	}

	/**
	 * Schakel een relevante rol in voor de kamer.
	 */
	@ValidateOnExecution
	public void activeerRelevanteRol(String rol) {
		for (Rol mogelijkeRol : Rol.values()) {
			if (mogelijkeRol.getRolNaam().equals(rol)) {
				this.relevanteRollen.add(mogelijkeRol);
			} else {
				throw new RoleNotFoundException();
			}
		}
	}

}
