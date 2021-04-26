package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.validation.constraints.NotNull;

@Embeddable
public class DeelnemerId implements Serializable {

	/*
	 * Using wrapper types instead of primitive types allows us to
	 * check if the fields were 'loaded' from the database by doing
	 * a null-check, this isn't possible with primitive types.
	 *
	 * See: https://stackoverflow.com/a/9146987
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deelnemerId;

	private Long spelId;

	public DeelnemerId() {
		// An empty constructor is required by JPA
	}

	public DeelnemerId(@NotNull Long spelId) {
		this.spelId = spelId;
	}

	public Long getDeelnemerId() {
		return deelnemerId;
	}

	public Long getSpelId() {
		return spelId;
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
		return deelnemerId.equals(that.deelnemerId) && spelId.equals(that.spelId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(deelnemerId, spelId);
	}

}
