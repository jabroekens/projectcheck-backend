package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deelnemerId;

	/**
	 * De code van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt.
	 */
	private String kamerCode;

	public DeelnemerId() {
		// Een lege constructor is vereist door JPA.
	}

	public DeelnemerId(@NotNull String kamerCode) {
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

	@Override
	public int hashCode() {
		return Objects.hash(deelnemerId, kamerCode);
	}

}
