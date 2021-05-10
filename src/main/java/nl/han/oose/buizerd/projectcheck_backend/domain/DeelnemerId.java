package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.io.Serializable;
import java.util.Objects;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;

/**
 * De identifier van {@link Deelnemer}.
 */
@Embeddable
public class DeelnemerId implements Serializable {

	/**
	 * Het ID van de {@link Deelnemer}.
	 */
	/*
	 * Er wordt gebruik gemaakt van wrapper types in plaats van
	 * primitieve types, zodat er gekeken kan worden of velden
	 * al geladen zijn uit de datastore doormiddel van een
	 * null-check. Bij `int` is het bijvoorbeeld niet mogelijk
	 * te weten of 0 betekent dat het niet geladen is, of dat
	 * het nog geladen moet worden.
	 *
	 * Zie: https://stackoverflow.com/a/9146987
	 */
	@NotNull
	@Column(nullable = false, updatable = false)
	private Long id;

	/**
	 * De code van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt.
	 */
	@KamerCode
	@Column(nullable = false, updatable = false)
	private String kamerCode;

	/**
	 * Construeert een {@link DeelnemerId}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	public DeelnemerId() {
	}

	/**
	 * Construeert een {@link DeelnemerId}.
	 *
	 * @param id Het ID van de deelnemer.
	 * @param kamerCode De code van de kamer.
	 */
	@ValidateOnExecution
	public DeelnemerId(@NotNull Long id, @KamerCode String kamerCode) {
		this.id = id;
		this.kamerCode = kamerCode;
	}

	/**
	 * Haal het ID van de {@link Deelnemer} op.
	 *
	 * @return Het ID van de deelnemer.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Haal de code van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt op.
	 *
	 * @return De code van de kamer.
	 */
	public String getKamerCode() {
		return kamerCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DeelnemerId that = (DeelnemerId) o;
		return id.equals(that.id) && kamerCode.equals(that.kamerCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(id, kamerCode);
	}

}
