package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;

public class HighlightVolgendeKaartEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		if (deelnemer instanceof Begeleider) {
			Kamer huidigeKamer = deelnemer.getKamer();
			Optional<Ronde> huidigeRonde = huidigeKamer.getHuidigeRonde();
			huidigeRonde.get().setGehighlighteKaart(null);
			return new EventResponse(EventResponse.Status.OK)
				.metContext("gehighlighteKaart", null).stuurNaarAlleClients();
		} else {
			return new EventResponse(EventResponse.Status.VERBODEN);
		}
	}

}
