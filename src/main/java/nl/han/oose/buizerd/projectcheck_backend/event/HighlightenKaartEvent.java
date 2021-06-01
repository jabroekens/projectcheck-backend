package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;

import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;

import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;

public class HighlightenKaartEvent extends Event {

	@NotNull
	KaartToelichting kaartToelichting;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		Ronde huidigeRonde = new Ronde();//deelnemer.getKamer().getHuidigeRonde();
		huidigeRonde.setGehighlighteKaart(kaartToelichting);
		return new EventResponse(EventResponse.Status.OK).antwoordOp(this).metContext("gehighlighteKaart", kaartToelichting).stuurNaarAlleClients();
	}

}
