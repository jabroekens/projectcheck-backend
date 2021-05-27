package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class KiesRelevanteRolEvent extends Event {

	@NotNull
	Set<Rol> rollen;

	@Override
	protected EventResponse voerUit(Kamer kamer, Session session) {
		try {
			Begeleider begeleider = kamer.getBegeleider();
			if (begeleider.getDeelnemerId().equals(super.getDeelnemerId())) {
				kamer.activeerRelevanteRollen(rollen);
				String bericht = String.format("de rollen %s zijn ingeschakeld voor de kamer %s", rollen, kamer.getKamerCode());
				return new EventResponse(EventResponse.Status.OK).metContext("bericht", bericht);
			} else {
				return new EventResponse(EventResponse.Status.VERBODEN).metContext("deelnemer", super.getDeelnemerId());
			}
		} catch (IllegalArgumentException e) {
			return new EventResponse(EventResponse.Status.ROL_NIET_GEVONDEN).metContext("rollen", rollen);
		}
	}

}
