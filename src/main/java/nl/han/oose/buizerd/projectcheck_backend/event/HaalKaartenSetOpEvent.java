package nl.han.oose.buizerd.projectcheck_backend.event;

import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerHeeftGeenRolException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

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
	public EventResponse voerUit(KamerService kamerService) {
		try {
			var deelnemerKaartenSets = kamerService.getKaartenSets(super.getDeelnemerId());
			return new EventResponse(EventResponse.Status.OK)
				.metContext("gekozen", deelnemerKaartenSets.isGekozen())
				.metContext("kaartensets", deelnemerKaartenSets.getKaartenSets());
		} catch (DeelnemerHeeftGeenRolException e) {
			return new EventResponse(EventResponse.Status.ROL_NIET_GEVONDEN);
		}
	}

}
