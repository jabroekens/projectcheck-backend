package nl.han.oose.buizerd.projectcheck_backend.event;

import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

/**
 * Zet de fase van de kamer waaraan de begeleider deelneemt naar de volgende fase en geeft deze terug.
 * <p>
 * Als de deelnemer geen begeleider is, dan wordt er een {@link EventResponse.Status#VERBODEN VERBODEN} status teruggegeven.
 */
public class VolgendeFaseEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer) {
		if (deelnemer instanceof Begeleider) {
			var kamer = deelnemer.getKamer();
			var kamerFase = kamer.getKamerFase();
			deelnemer.getKamer().setKamerFase(kamerFase.getVolgendeFase());

			return new EventResponse(EventResponse.Status.OK).metContext("volgendeFase", kamer.getKamerFase());
		}

		return new EventResponse(EventResponse.Status.VERBODEN);
	}

	@Override
	protected void handelAf(DAO dao, Kamer kamer) {
		dao.update(kamer);
	}

}
