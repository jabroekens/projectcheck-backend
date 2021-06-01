package nl.han.oose.buizerd.projectcheck_backend.event;

import com.google.gson.Gson;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import java.io.IOException;
import java.net.http.WebSocket;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

public class HighlightenKaartEvent extends Event {

	@NotNull
	KaartToelichting kaartToelichting;


	@Override
	protected  EventResponse voerUit(Deelnemer deelnemer, Session session) {
		Ronde huidigeRonde = deelnemer.getKamer().getHuidigeRonde();
		huidigeRonde.setGehighlighteKaart(kaartToelichting);
		return new EventResponse(EventResponse.Status.OK).antwoordOp(this).metContext("gehighlighteKaart",kaartToelichting).stuurNaarAlleClients();
	}

}
