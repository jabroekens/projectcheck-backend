package nl.han.oose.buizerd.projectcheck_backend.domain;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.validation.constraints.NotNull;

@Entity
public class Deelnemer {

	@EmbeddedId
	private DeelnemerId deelnemerId;

	@MapsId("spelId")
	@ManyToOne(fetch = FetchType.LAZY)
	private Spel spel;

	@Column(updatable = false)
	private String naam;

	public Deelnemer() {
		// An empty constructor is required by JPA
	}

	public Deelnemer(@NotNull DeelnemerId deelnemerId, @NotNull String naam) {
		this.deelnemerId = deelnemerId;
		this.naam = naam;
	}

	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	public String getNaam() {
		return naam;
	}

}
