package nl.han.oose.buizerd.projectcheck_backend.domain;

import com.google.gson.annotations.Expose;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.util.HashSet;
import java.util.Set;

/**
 * Een rol is een set van rechten die een {@link Deelnemer} aangewezen kan krijgen of kan kiezen binnen een {@link Kamer}.
 * <p>
 * Elke rol heeft een naam.
 */
@Entity
public class Rol {

	/**
	 * De naam van de rol.
	 */
	@Expose
	@NotNull
	@Id
	private String rolNaam;

	/**
	 * Alle {@link KaartenSet}'s die bij deze rol horen.
	 */
	@OneToMany(mappedBy = "rol")
	private Set<KaartenSet> kaartenSets;

	/**
	 * Construeert een {@link Rol}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	public Rol() {
	}

	/**
	 * Construeert een {@link Rol}.
	 *
	 * @param rolNaam De naam van de rol.
	 */
	@ValidateOnExecution
	public Rol(@NotNull String rolNaam) {
		this.rolNaam = rolNaam;
		this.kaartenSets = new HashSet<>();
	}

	/**
	 * Haal de naam van de rol op.
	 *
	 * @return De naam van de rol.
	 */
	public String getRolNaam() {
		return rolNaam;
	}

	/**
	 * Geeft de kaartenset die bij de rol past terug.
	 *
	 * @return De kaartensets.
	 */
	public Set<KaartenSet> getKaartenSets() {
		return kaartenSets;
	}

}
