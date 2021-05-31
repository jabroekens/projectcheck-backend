package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
	@Column(nullable = false, updatable = false)
	private Integer id;

	/**
	 * Een set van meerdere kaarten.
	 * De kaarten zijn niet nullable, maar kan wel worden aangepast.
	 */
	@ManyToMany
	@Column(nullable = false)
	private Set<Kaart> kaarten;

	/**
	 * Alle {@link Rol}len die bij deze kaartenset horen.
	 */
	@ManyToMany
	@Column(nullable = false)
	private Set<Rol> rollen;


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
	public KaartenSet(Set<Rol> rollen, Set<Kaart> kaarten) {
		this.rollen = rollen;
		this.kaarten = kaarten;
	}

	/**
	 * Geeft de auto incremented id van de kaartenset terug.
	 * @return De auto incremented id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Geeft de kaarten terug die de kaartenset heeft.
	 * @return De kaartenset.
	 */
	public Set<Kaart> getKaarten() {
		return kaarten;
	}

	/**
	 * Geeft de rollen van de kaartenset terug.
	 * @return De rollen van de kaartenset.
	 */
	public Set<Rol> getRollen() {
		return rollen;
	}

}
