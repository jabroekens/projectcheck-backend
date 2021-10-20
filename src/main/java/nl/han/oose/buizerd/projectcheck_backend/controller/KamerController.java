package nl.han.oose.buizerd.projectcheck_backend.controller;

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
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

@ServerEndpoint(value = "/kamer/{kamerCode}", decoders = {Event.Decoder.class})
public class KamerController {

	// Package-private zodat het getest kan worden
	static Logger logger = Logger.getLogger(KamerController.class.getName());

	private KamerService kamerService;

	@OnOpen
	public void open(Session session, EndpointConfig config, @PathParam("kamerCode") String kamerCode)
	throws IOException {
		if (!kamerService.kanDeelnemen(kamerCode)) {
			session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kan niet deelnemen aan kamer."));
		}
	}

	@OnClose
	public void close(Session session, CloseReason closeReason, @PathParam("kamerCode") String kamerCode) {
		// XXX Behandelen als iemand (onverwachts) een kamer verlaat, bijv.
	}

	@OnMessage
	public void message(Event event, @PathParam("kamerCode") String kamerCode, Session session) throws IOException {
		try {
			kamerService.voerEventUit(event)
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
		} catch (DeelnemerNietGevondenException vte) {
			session.getBasicRemote().sendText(
				new EventResponse(EventResponse.Status.VERBODEN).antwoordOp(event).asJson()
			);
		} catch (KamerNietGevondenException kne) {
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
			 * Voer uit in een try/catch-statement zodat `KamerController#error(Session, Throwable, String)`
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
	public void setKamerService(KamerService kamerService) {
		this.kamerService = kamerService;
	}

}
