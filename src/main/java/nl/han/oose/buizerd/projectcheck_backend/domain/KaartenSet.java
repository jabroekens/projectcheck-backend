package nl.han.oose.buizerd.projectcheck_backend.domain;

import com.google.gson.annotations.Expose;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.ExcludeFromGeneratedCoverageReport;

/**
 * Een {@link KaartenSet} is een collectie van kaarten horend bij een bepaalde rol.
 */
@Entity
public class KaartenSet {

	@Id
	@Column(nullable = false, updatable = false)
	private Long id;

	@Expose
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Set<Kaart> kaarten;

	@ManyToOne
	private Rol rol;

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@ExcludeFromGeneratedCoverageReport(reason = "wordt gebruikt door JPA en mag niet aangeroepen worden")
	@Deprecated
	protected KaartenSet() {
	}

	public KaartenSet(@NotNull Long id, @NotNull @Valid Rol rol, @NotNull Set<@Valid Kaart> kaarten) {
		this.id = id;
		this.rol = rol;
		this.kaarten = kaarten;
	}

	public Long getId() {
		return id;
	}

	public Set<Kaart> getKaarten() {
		return kaarten;
	}

	public Rol getRol() {
		return rol;
	}

}
