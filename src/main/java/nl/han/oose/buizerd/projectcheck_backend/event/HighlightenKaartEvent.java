package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;

import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;

import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;

public class HighlightenKaartEvent extends Event {

	@NotNull
	@Valid
	KaartToelichting kaartToelichting;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		Kamer huidigeKamer = deelnemer.getKamer();
		Optional<Ronde> huidigeRonde = huidigeKamer.getHuidigeRonde();
		if (!huidigeRonde.get().getGehighlighteKaart().isPresent()) {
			huidigeRonde.get().setGehighlighteKaart(kaartToelichting);
			return new EventResponse(EventResponse.Status.OK).metContext("gehighlighteKaart", kaartToelichting).stuurNaarAlleClients();
		}
		else return new EventResponse(EventResponse.Status.VERBODEN);
	}


}
