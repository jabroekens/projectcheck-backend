package nl.han.oose.buizerd.projectcheck_backend.domain;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import nl.han.oose.buizerd.projectcheck_backend.ExcludeFromGeneratedCoverageReport;

/**
 * Een {@link KaartToelichting} is de toelichting die een {@link Deelnemer}
 * geeft bij de door hen gekozen {@link Kaart Kaart(en)}.
 */
@Entity
public class KaartToelichting {

	@Expose
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Expose
	@OneToOne
	@Column(nullable = false, updatable = false)
	private Kaart kaart;

	@Expose
	@NotEmpty
	@Column(nullable = false)
	private String toelichting;

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@ExcludeFromGeneratedCoverageReport(reason = "wordt gebruikt door JPA en mag niet aangeroepen worden")
	@Deprecated
	protected KaartToelichting() {
	}

	public KaartToelichting(Kaart kaart, String toelichting) {
		this.kaart = kaart;
		this.toelichting = toelichting;
	}

	/**
	 * @return het ID van de kaart of {@code null} als deze nog niet gegenereerd is
	 */
	public Integer getId() {
		return id;
	}

	public Kaart getKaart() {
		return kaart;
	}

	public String getToelichting() {
		return toelichting;
	}

	public void setToelichting(@NotEmpty String toelichting) {
		this.toelichting = toelichting;
	}

}
