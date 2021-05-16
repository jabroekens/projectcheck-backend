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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

@ServerEndpoint(value = "/kamer/{kamerCode}", decoders = {Event.Decoder.class})
public class KamerService {

	// package-private zodat het getest kan worden
	static final Logger LOGGER;
	static final Set<String> GEREGISTREERDE_KAMERS;

	static {
		LOGGER = Logger.getLogger(KamerService.class.getName());
		GEREGISTREERDE_KAMERS = new HashSet<>();
	}

	/**
	 * Registreert een {@link Kamer} en stelt een WebSocket URL beschikbaar.
	 *
	 * @param kamerCode De code van een kamer.
	 * @see KamerService#getUrl(String)
	 */
	public static void registreer(String kamerCode) {
		KamerService.GEREGISTREERDE_KAMERS.add(kamerCode);
	}

	@Context
	private UriInfo uriInfo;

	@Inject
	private KamerRepository kamerRepository;

	/**
	 * Construeert een {@link KamerService}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door Jakarta WebSockets API en mag niet aangeroepen worden.</b>
	 */
	public KamerService() {
	}

	/**
	 * Construeert een {@link KamerService}.
	 *
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 *
	 * @param uriInfo Een implementatie van {@link UriInfo}.
	 * @param kamerRepository Een {@link KamerRepository}.
	 */
	KamerService(UriInfo uriInfo, KamerRepository kamerRepository) {
		this.uriInfo = uriInfo;
		this.kamerRepository = kamerRepository;
	}

	/**
	 * Haalt de WebSocket URL op voor de kamer met de code {@code kamerCode}.
	 *
	 * @param kamerCode De code van een kamer.
	 * @return De WebSocket URL van de kamer.
	 */
	@SuppressWarnings("brain-overload")
	public String getUrl(String kamerCode) {
		URI uri = uriInfo.getBaseUri();

		return UriBuilder
			.fromUri(uri)
			.replacePath(KamerService.class.getAnnotation(ServerEndpoint.class).value())
			.scheme("https".equals(uri.getScheme()) ? "wss" : "ws")
			.build(kamerCode).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@OnOpen
	public void open(Session session, EndpointConfig config, @PathParam("kamerCode") String kamerCode) {
		if (!KamerService.GEREGISTREERDE_KAMERS.contains(kamerCode)) {
			try {
				session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kamer niet gevonden"));
			} catch (IOException e) {
				KamerService.LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
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
		String eventKamerCode = event.getDeelnemerId().getKamerCode();
		if (!eventKamerCode.equals(kamerCode)) {
			EventResponse response = new EventResponse(EventResponse.Status.VERBODEN).AntwoordOp(event);
			session.getBasicRemote().sendText(response.asJson());
			return;
		}

		Optional<Kamer> kamer = kamerRepository.get(eventKamerCode);
		if (kamer.isPresent()) {
			event.voerUit(kamerRepository, kamer.get(), session);
		} else {
			EventResponse response = new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN)
				.metContext("kamerCode", eventKamerCode)
				.AntwoordOp(event);
			session.getBasicRemote().sendText(response.asJson());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@OnError
	public void error(Session session, Throwable error, @PathParam("kamerCode") String kamerCode) {
		if (error instanceof IllegalArgumentException) {
			/*
			 * Voer uit in een try/catch-statement zodat `KamerService#error(Session, Throwable, String)`
			 * niet opnieuw aangeroepen wordt, wat tot een StackOverflowError zou leiden.
			 */
			try {
				session.getBasicRemote().sendText(new EventResponse(EventResponse.Status.INVALIDE).asJson());
			} catch (IOException e) {
				KamerService.LOGGER.log(Level.SEVERE, e.getMessage(), e);
			}
		}

		KamerService.LOGGER.log(Level.SEVERE, error.getMessage(), error);
	}

}
