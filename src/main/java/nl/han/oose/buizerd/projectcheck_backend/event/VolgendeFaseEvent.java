package nl.han.oose.buizerd.projectcheck_backend.event;

import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

/**
 * Zet de fase van de kamer waaraan de begeleider deelneemt naar de volgende fase en geeft deze terug.
 * <p>
 * Als de deelnemer geen begeleider is, dan wordt er een {@link EventResponse.Status#VERBODEN VERBODEN} status
 * teruggegeven.
 */
public class VolgendeFaseEvent extends Event {

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		try {
			var volgendeFase = kamerService.naarVolgendeFase(super.getDeelnemerId());
			return new EventResponse(EventResponse.Status.OK).metContext("volgendeFase", volgendeFase);
		} catch (VerbodenToegangException e) {
			return new EventResponse(EventResponse.Status.VERBODEN);
		}
	}

}
