package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class GetRelevanteRollenEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		Kamer kamer = deelnemer.getKamer();
		Set<Rol> rollen = kamer.getRelevanteRollen();
		kamer.activeerRelevanteRollen(rollen);
		return new EventResponse(EventResponse.Status.OK).metContext("geefRollen", rollen);
	}

}
