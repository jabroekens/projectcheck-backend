package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class KiesRolEvent extends Event {

	@NotNull
	@Valid
	Rol rol;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		deelnemer.setRol(rol);
		return new EventResponse(EventResponse.Status.OK).metContext("gekozenRol", rol);
	}

	@Override
	protected void handelAf(DAO dao, Kamer kamer) {
		dao.update(kamer);
	}

}
