package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class KiesRelevanteRollenEvent extends Event {

	@NotNull
	Set<@NotNull @Valid Rol> relevanteRollen;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		if (deelnemer instanceof Begeleider) {
			deelnemer.getKamer().setRelevanteRollen(relevanteRollen);

			return new EventResponse(EventResponse.Status.OK).metContext(
				"bericht",
				String.format("de rollen %s zijn ingeschakeld voor de kamer %s", relevanteRollen, deelnemer.getKamer().getKamerCode())
			);
		} else {
			return new EventResponse(EventResponse.Status.VERBODEN).metContext("deelnemer", deelnemer.getDeelnemerId());
		}
	}

	@Override
	protected void handelAf(DAO dao, Kamer kamer) {
		dao.update(kamer);
	}

}
