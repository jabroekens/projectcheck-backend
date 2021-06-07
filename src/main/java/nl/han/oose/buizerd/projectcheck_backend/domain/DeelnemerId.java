package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.io.Serializable;
import java.util.Objects;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;

/**
 * De composite primary key van {@link Deelnemer}.
 */
@Embeddable
public class DeelnemerId implements Serializable {

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

	/**
	 * @see Kamer#genereerDeelnemerId()
	 */
	@NotNull
	@Column(nullable = false, updatable = false)
	private Long id;

	@KamerCode
	@Column(nullable = false, updatable = false)
	private String kamerCode;

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@Deprecated
	public DeelnemerId() {
	}

	@ValidateOnExecution
	public DeelnemerId(@NotNull Long id, @KamerCode String kamerCode) {
		this.id = id;
		this.kamerCode = kamerCode;
	}

	public Long getId() {
		return id;
	}

	public String getKamerCode() {
		return kamerCode;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DeelnemerId)) {
			return false;
		}
		DeelnemerId that = (DeelnemerId) o;
		return Objects.equals(getId(), that.getId()) && Objects.equals(getKamerCode(), that.getKamerCode());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, kamerCode);
	}

}
