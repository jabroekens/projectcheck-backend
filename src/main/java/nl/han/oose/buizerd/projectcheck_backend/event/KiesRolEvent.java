package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class KiesRolEvent extends Event {

	@NotNull
	@Valid
	Rol rol;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		deelnemer.setRol(rol);
		EventResponse antwoord = new EventResponse(EventResponse.Status.OK);
		antwoord.antwoordOp(this);
		antwoord.metContext("gekozenRol", rol);
		return antwoord;
	}

}
