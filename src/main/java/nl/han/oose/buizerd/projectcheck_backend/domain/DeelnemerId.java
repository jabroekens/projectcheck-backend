package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

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
	@Column(nullable = false, updatable = false)
	private Long deelnemerId;

	/**
	 * De code van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt.
	 */
	@Column(nullable = false, updatable = false)
	private String kamerCode;

	/**
	 * Construeert een {@link DeelnemerId}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	protected DeelnemerId() {
	}

	public DeelnemerId(@NotNull Kamer kamer) {
		this.deelnemerId = kamer.getAantalDeelnemers() + 1L;
		this.kamerCode = kamer.getKamerCode();
	}

	/**
	 * Construeert een {@link DeelnemerId} met een specifiek ID en kamer code.
	 * <p>
	 * <b>Deze constructor mag alleen gebruikt worden voor unit tests.</b>
	 *
	 * @param deelnemerId Het ID van de deelnemer.
	 * @param kamerCode De code van de kamer.
	 */
	DeelnemerId(@NotNull Long deelnemerId, @NotNull String kamerCode) {
		this.deelnemerId = deelnemerId;
		this.kamerCode = kamerCode;
	}

	/**
	 * Haal het ID van de {@link Deelnemer} op.
	 *
	 * @return Het ID van de deelnemer.
	 */
	public Long getDeelnemerId() {
		return deelnemerId;
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
		return deelnemerId.equals(that.deelnemerId) && kamerCode.equals(that.kamerCode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(deelnemerId, kamerCode);
	}

}
