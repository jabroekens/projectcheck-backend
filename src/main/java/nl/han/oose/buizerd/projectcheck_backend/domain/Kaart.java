package nl.han.oose.buizerd.projectcheck_backend.domain;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.util.Objects;
import nl.han.oose.buizerd.projectcheck_backend.ExcludeFromGeneratedCoverageReport;

/**
 * Een {@link Kaart} kan ingezet worden door een {@link Deelnemer}.
 * <p>
 * Elke kaart heeft een code en een beschrijving.
 */
@Entity
public class Kaart {

	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Expose
	@NotNull
	@Column(nullable = false, updatable = false)
	private Integer code;

	@Expose
	@NotNull
	@Column(nullable = false, updatable = false)
	private String text;

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@ExcludeFromGeneratedCoverageReport(reason = "wordt gebruikt door JPA en mag niet aangeroepen worden")
	@Deprecated
	protected Kaart() {
	}

	@ValidateOnExecution
	public Kaart(@NotNull Integer code, @NotNull String text) {
		this.code = code;
		this.text = text;
	}

	/**
	 * @return het ID van de kaart of {@code null} als deze nog niet gegenereerd is
	 */
	public Integer getId() {
		return id;
	}

	public int getCode() {
		return code;
	}

	public String getText() {
		return text;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Kaart)) {
			return false;
		}
		var that = (Kaart) o;
		return Objects.equals(getCode(), that.getCode()) && Objects.equals(getText(), that.getText());
	}

	@Override
	public int hashCode() {
		return Objects.hash(code, text);
	}

}
