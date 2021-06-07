package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.inject.Inject;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;

@ServerEndpoint(value = "/kamer/{kamerCode}", decoders = {Event.Decoder.class})
public class KamerService {

	// Package-private zodat het getest kan worden
	static Logger logger = Logger.getLogger(KamerService.class.getName());

	private DAO dao;

	@OnOpen
	public void open(Session session, EndpointConfig config, @PathParam("kamerCode") @KamerCode String kamerCode) throws IOException {
		Optional<Kamer> kamer = dao.read(Kamer.class, kamerCode);
		if (kamer.isEmpty() || kamer.get().getKamerFase() == KamerFase.GESLOTEN) {
			session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kamer is gesloten of niet gevonden."));
		}
	}

	@OnClose
	public void close(Session session, CloseReason closeReason, @PathParam("kamerCode") String kamerCode) {
		// XXX Behandelen als iemand (onverwachts) een kamer verlaat, bijv.
	}

	@OnMessage
	public void message(Event event, @PathParam("kamerCode") String kamerCode, Session session) throws IOException {
		Optional<Kamer> kamer = dao.read(Kamer.class, kamerCode);
		if (kamer.isPresent()) {
			Optional<Deelnemer> deelnemer = kamer.get().getDeelnemer(event.getDeelnemerId());

			if (deelnemer.isPresent()) {
				event.voerUit(dao, deelnemer.get(), session)
				     .whenComplete((response, exception) -> {
					     if (exception != null) {
						     /*
						      * We moeten de methode zelf aanroepen omdat de exception
						      * wordt gegooit in een andere thread, waardoor deze
						      * niet automatisch wordt aangeroepen.
						      */
						     error(session, exception, kamerCode);
						     return;
					     }

					     if (response.isStuurNaarAlleClients()) {
						     session.getOpenSessions().stream()
						            .filter(Session::isOpen)
						            .forEach(s -> s.getAsyncRemote().sendText(response.asJson()));
					     } else if (session.isOpen()) {
						     session.getAsyncRemote().sendText(response.asJson());
					     }
				     });
			} else {
				session.getBasicRemote().sendText(
					new EventResponse(EventResponse.Status.VERBODEN).antwoordOp(event).asJson()
				);
			}
		} else {
			session.getBasicRemote().sendText(
				new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN)
					.metContext("kamerCode", kamerCode).antwoordOp(event).asJson()
			);
		}
	}

	@OnError
	public void error(Session session, Throwable error, @PathParam("kamerCode") String kamerCode) {
		if (error.getCause() != null) {
			error = error.getCause();
		}

		if (error instanceof IllegalArgumentException) {
			/*
			 * Voer uit in een try/catch-statement zodat `KamerService#error(Session, Throwable, String)`
			 * niet opnieuw aangeroepen wordt, wat tot een StackOverflowError zou leiden.
			 */
			try {
				session.getBasicRemote().sendText(new EventResponse(EventResponse.Status.ONGELDIG).asJson());
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return;
			}
		}

		logger.log(Level.SEVERE, error.getMessage(), error);
	}

	@Inject
	public void setDao(DAO dao) {
		this.dao = dao;
	}

}
