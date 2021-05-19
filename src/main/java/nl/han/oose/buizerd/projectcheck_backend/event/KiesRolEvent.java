package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class KiesRolEvent extends Event {

	@NotNull
	@Valid
	Rol rol;

	@Override
	protected EventResponse voerUit(Kamer kamer, Session session) {
		Optional<Deelnemer> deelnemer = kamer.getDeelnemer(super.getDeelnemerId());
		if (deelnemer.isPresent()) {
			deelnemer.get().setRol(rol);
			EventResponse antwoord = new EventResponse(EventResponse.Status.OK);
			antwoord.antwoordOp(this);
			antwoord.metContext("gekozenRol", rol);
			return antwoord;
		}
		return new EventResponse(EventResponse.Status.DEELNEMER_NIET_GEVONDEN);
	}

}
