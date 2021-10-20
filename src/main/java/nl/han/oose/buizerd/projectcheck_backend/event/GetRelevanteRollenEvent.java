package nl.han.oose.buizerd.projectcheck_backend.event;

import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

/**
 * Haalt de relevante rollen van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt op en geeft deze terug.
 */
public class GetRelevanteRollenEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer) {
		var kamer = deelnemer.getKamer();
		var rollen = kamer.getRelevanteRollen();
		return new EventResponse(EventResponse.Status.OK).metContext("geefRollen", rollen);
	}

}
