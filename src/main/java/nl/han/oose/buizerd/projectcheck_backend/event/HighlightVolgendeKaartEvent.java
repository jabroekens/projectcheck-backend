package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;

import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;

import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;

public class HighlightVolgendeKaartEvent extends Event {


	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		Ronde huidigeRonde = new Ronde(); //deelnemer.getKamer().getHuidigeRonde();
		huidigeRonde.setGehighlighteKaart(null);
		return new EventResponse(EventResponse.Status.OK)
			.antwoordOp(this)
			.metContext("gehighlighteKaart",null).stuurNaarAlleClients();
	}

}
