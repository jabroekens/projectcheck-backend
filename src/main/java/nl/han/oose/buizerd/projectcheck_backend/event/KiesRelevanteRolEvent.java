package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class KiesRelevanteRolEvent extends Event {

	@NotNull
	String rolNaam;

	@Override
	protected EventResponse voerUit(Kamer kamer, Session session) {
		try {
			Rol rol = Rol.valueOf(rolNaam);
			Begeleider begeleider = kamer.getBegeleider();
			if (begeleider.getDeelnemerId().equals(super.getDeelnemerId())) {
				kamer.activeerRelevanteRol(rol);
				String bericht = String.format("de rol %s is ingeschakeld voor de kamer %s", rol, kamer.getKamerCode());
				return new EventResponse(EventResponse.Status.OK).metContext("bericht", bericht);
			} else {
				return new EventResponse(EventResponse.Status.VERBODEN).metContext("deelnemer", super.getDeelnemerId());
			}
		} catch (IllegalArgumentException e) {
			return new EventResponse(EventResponse.Status.ROL_NIET_GEVONDEN).metContext("rolNaam", rolNaam);
		}
	}

}
