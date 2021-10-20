package nl.han.oose.buizerd.projectcheck_backend.event;

import java.util.HashSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;

/**
 * Haalt de {@link KaartenSet KaartenSet(s)} op die horen bij de {@link Rol} van de {@link Deelnemer} en geeft deze terug.
 * <p>
 * Als de deelnemer geen rol heeft, dan wordt er een {@link EventResponse.Status#ROL_NIET_GEVONDEN ROL_NIET_GEVONDEN}
 * status teruggegeven.
 * <p>
 * Als de deelnemer de rol {@link StandaardRol#PROJECTBUREAU Projectbureau} heeft, dan worden de kaartenset(s)
 * teruggegeven voor alle relevante rollen van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt.
 */
public class HaalKaartenSetOpEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer) {
		if (deelnemer.getRol() == null) {
			return new EventResponse(EventResponse.Status.ROL_NIET_GEVONDEN);
		}

		var kaartenSets = new HashSet<KaartenSet>();
		var gekozen = false;

		if (deelnemer.getRol().equals(StandaardRol.PROJECTBUREAU.getRol())) {
			for (Rol kamerRol : deelnemer.getKamer().getRelevanteRollen()) {
				kaartenSets.addAll(kamerRol.getKaartenSets());
			}
		} else {
			kaartenSets.addAll(deelnemer.getRol().getKaartenSets());
			gekozen = true;
		}

		return new EventResponse(EventResponse.Status.OK)
			       .metContext("gekozen", gekozen)
			       .metContext("kaartensets", kaartenSets);
	}

}
