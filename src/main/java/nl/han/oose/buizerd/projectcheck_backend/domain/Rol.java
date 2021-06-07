package nl.han.oose.buizerd.projectcheck_backend.domain;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.executable.ValidateOnExecution;
import java.util.HashSet;
import java.util.Set;

/**
 * Een rol wordt vertegenwoordigd door een {@link Deelnemer}.
 * <p>
 * Elke rol heeft een naam en een beschrijving.
 */
@Entity
public class Rol {

	@Expose
	@NotEmpty
	@Id
	private String rolNaam;

	@Expose
	@NotEmpty
	private String beschrijving;

	@OneToMany(mappedBy = "rol", fetch = FetchType.EAGER)
	private Set<KaartenSet> kaartenSets = new HashSet<>();

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@Deprecated
	protected Rol() {
	}

	@ValidateOnExecution
	public Rol(@NotEmpty String rolNaam, @NotEmpty String beschrijving) {
		this.rolNaam = rolNaam;
		this.beschrijving = beschrijving;
	}

	public String getRolNaam() {
		return rolNaam;
	}

	public String getBeschrijving() {
		return beschrijving;
	}

	public Set<KaartenSet> getKaartenSets() {
		return kaartenSets;
	}

}
