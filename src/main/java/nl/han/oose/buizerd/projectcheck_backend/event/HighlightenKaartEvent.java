package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;

public class HighlightenKaartEvent extends Event {

	@NotNull
	@Valid
	KaartToelichting kaartToelichting;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		var huidigeRonde = deelnemer.getKamer().getHuidigeRonde();

		if (huidigeRonde.isPresent()) {
			if (huidigeRonde.get().getGehighlighteKaart().isEmpty()) {
				huidigeRonde.get().setGehighlighteKaart(kaartToelichting);
				return new EventResponse(EventResponse.Status.OK)
					       .metContext("gehighlighteKaart", kaartToelichting)
					       .stuurNaarAlleClients();
			} else {
				return new EventResponse(EventResponse.Status.VERBODEN);
			}
		} else {
			return new EventResponse(EventResponse.Status.RONDE_NIET_GEVONDEN);
		}
	}

}
