package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;

public class GetRelevanteRollenEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		var kamer = deelnemer.getKamer();
		var rollen = kamer.getRelevanteRollen();
		return new EventResponse(EventResponse.Status.OK).metContext("geefRollen", rollen);
	}

}
