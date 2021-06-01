package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;

/**
 * Zet de fase van de kamer waaraan de begeleider deelneemt
 * naar de volgende fase en geeft deze terug.
 * <p>
 * Als de deelnemer geen begeleider is, dan wordt er een
 * {@code VERBODEN} status teruggegeven.
 */
public class VolgendeFaseEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		if (deelnemer instanceof Begeleider) {
			Kamer kamer = deelnemer.getKamer();
			KamerFase kamerFase = kamer.getKamerFase();
			deelnemer.getKamer().setKamerFase(kamerFase.volgendeFase());

			return new EventResponse(EventResponse.Status.OK).metContext("volgendeFase", kamer.getKamerFase());
		}

		return new EventResponse(EventResponse.Status.VERBODEN);
	}

	@Override
	protected void handelAf(DAO dao, Kamer kamer) {
		dao.update(kamer);
	}

}
