package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

/**
 * Zet de relevante rollen van de {@link Kamer} waaraan de {@link Deelnemer} deelneemt.
 * <p>
 * Als de deelnemer geen begeleider is, dan wordt er een {@link EventResponse.Status#VERBODEN VERBODEN} status
 * teruggegeven.
 */
public class KiesRelevanteRollenEvent extends Event {

	@NotNull
	Set<@NotNull @Valid Rol> relevanteRollen;

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		try {
			kamerService.kiesRelevanteRollen(super.getDeelnemerId(), relevanteRollen);
			return new EventResponse(EventResponse.Status.OK);
		} catch (VerbodenToegangException e) {
			return new EventResponse(EventResponse.Status.VERBODEN);
		}
	}

}
