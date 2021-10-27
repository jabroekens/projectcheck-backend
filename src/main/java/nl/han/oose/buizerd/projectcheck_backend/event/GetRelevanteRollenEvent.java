package nl.han.oose.buizerd.projectcheck_backend.event;

import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

/**
 * Haalt de relevante rollen van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt op en geeft deze terug.
 */
public class GetRelevanteRollenEvent extends Event {

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		var relevanteRollen = kamerService.getRelevanteRollen(super.getDeelnemerId().getKamerCode());
		return new EventResponse(EventResponse.Status.OK).metContext("geefRollen", relevanteRollen);
	}

}
