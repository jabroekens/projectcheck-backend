package nl.han.oose.buizerd.projectcheck_backend.dto;

import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

/**
 * Het resultaat van {@link KamerService#getKaartenSets(DeelnemerId)}.
 */
public class DeelnemerKaartenSets {

	private final Set<KaartenSet> kaartenSets;
	private final boolean gekozen;

	public DeelnemerKaartenSets(Set<KaartenSet> kaartenSets, boolean gekozen) {
		this.kaartenSets = kaartenSets;
		this.gekozen = gekozen;
	}

	public Set<KaartenSet> getKaartenSets() {
		return kaartenSets;
	}

	/**
	 * @return {@code true} als de {@link KaartenSet kaartensets} van de {@link Deelnemer} afkomstig zijn van gekozen
	 *         {@link Rol rollen}
	 */
	public boolean isGekozen() {
		return gekozen;
	}

}
