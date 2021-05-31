package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.Set;

/**
 * Een {@link KaartenSet} is een onderdeel van de ProjectCheck die een bundel
 * kaarten heeft. Deze bundel wordt vervolgens opgeslagen binnen de rol.
 * TODO : @link de rol, wanneer nathan het heeft gecommit.
 */
@Entity
public class KaartenSet {

	/**
	 * Een unieke auto incremented code die de kaartenset onderscheid.
	 * De code is niet nullable en kan ook niet meer aangepast worden.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Een set van meerdere kaarten.
	 * De kaarten zijn niet nullable, maar kan wel worden aangepast.
	 */
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
	private Set<Kaart> kaarten;

	/**
	 * De {@link Rol} die bij deze kaartenset horen.
	 */
	@ManyToOne
	private Rol rol;

	/**
	 * Genereert een {@link KaartenSet}.
	 * Deze constructor zal alleen gebruikt worden door JPA.
	 */
	public KaartenSet() {
	}

	/**
	 * Genereert een {@link KaartenSet}.
	 *
	 * @param kaarten De kaarten voor de kaartenset.
	 */
	public KaartenSet(Rol rol, Set<Kaart> kaarten) {
		this.rol = rol;
		this.kaarten = kaarten;
	}

	/**
	 * Geeft de auto incremented id van de kaartenset terug.
	 *
	 * @return De auto incremented id.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Geeft de kaarten terug die de kaartenset heeft.
	 *
	 * @return De kaartenset.
	 */
	public Set<Kaart> getKaarten() {
		return kaarten;
	}

	/**
	 * Geeft de rollen van de kaartenset terug.
	 *
	 * @return De rollen van de kaartenset.
	 */
	public Rol getRol() {
		return rol;
	}

}
