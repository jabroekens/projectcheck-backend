package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;

public class HighlightVolgendeKaartEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		if (deelnemer instanceof Begeleider) {
			var huidigeRonde = deelnemer.getKamer().getHuidigeRonde();

			if (huidigeRonde.isPresent()) {
				huidigeRonde.get().setGehighlighteKaart(null);

				return new EventResponse(EventResponse.Status.OK)
					       .metContext("gehighlighteKaart", null)
					       .stuurNaarAlleClients();
			} else {
				return new EventResponse(EventResponse.Status.RONDE_NIET_GEVONDEN);
			}
		} else {
			return new EventResponse(EventResponse.Status.VERBODEN);
		}
	}

}
