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

	private final DAO<Kamer, String> kamerDAO;

	/**
	 * Construeert een {@link KamerService}.
	 *
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 *
	 * @param kamerDAO Een {@link DAO} voor {@link Kamer}.
	 */
	@Inject
	public KamerService(DAO<Kamer, String> kamerDAO) {
		this.kamerDAO = kamerDAO;
	}

	/**
	 * {@inheritDoc}
	 */
	@OnOpen
	public void open(Session session, EndpointConfig config, @PathParam("kamerCode") @KamerCode String kamerCode) throws IOException {
		Optional<Kamer> kamer = kamerDAO.read(Kamer.class, kamerCode);
		if (kamer.isEmpty() || kamer.get().getKamerFase() == KamerFase.GESLOTEN) {
			session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kamer is gesloten of niet gevonden."));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@OnClose
	public void close(Session session, CloseReason closeReason, @PathParam("kamerCode") String kamerCode) {
		// XXX Behandelen als iemand (onverwachts) een kamer verlaat, bijv.
	}

	/**
	 * {@inheritDoc}
	 */
	@OnMessage
	public void message(Event event, @PathParam("kamerCode") String kamerCode, Session session) throws IOException {
		Optional<Kamer> kamer = kamerDAO.read(Kamer.class, kamerCode);
		if (kamer.isPresent()) {
			Optional<Deelnemer> deelnemer = kamer.get().getDeelnemer(event.getDeelnemerId());

			if (deelnemer.isPresent()) {
				event.voerUit(kamerDAO, deelnemer.get(), session);
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

	/**
	 * {@inheritDoc}
	 */
	@OnError
	public void error(Session session, Throwable error, @PathParam("kamerCode") String kamerCode) {
		Logger logger = Logger.getLogger(KamerService.class.getName());

		if (error instanceof IllegalArgumentException) {
			/*
			 * Voer uit in een try/catch-statement zodat `KamerService#error(Session, Throwable, String)`
			 * niet opnieuw aangeroepen wordt, wat tot een StackOverflowError zou leiden.
			 */
			try {
				session.getBasicRemote().sendText(new EventResponse(EventResponse.Status.INVALIDE).asJson());
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		logger.log(Level.SEVERE, error.getMessage(), error);
	}

}
