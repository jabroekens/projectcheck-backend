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

@ServerEndpoint(
	value = "/kamer/{kamerCode}",
	decoders = {Event.Decoder.class},
	encoders = {EventResponse.Encoder.class}
)
public class KamerService {

	private static final Logger LOGGER = Logger.getLogger(KamerService.class.getName());

	private static final Set<String> geregistreerdeKamers;

	static {
		geregistreerdeKamers = new HashSet<>();
	}

	/**
	 * Registreert een {@link Kamer} en stelt een WebSocket URL beschikbaar.
	 *
	 * @param kamerCode De code van een kamer.
	 * @see KamerService#getUrl(String)
	 */
	public static void registreer(String kamerCode) {
		KamerService.geregistreerdeKamers.add(kamerCode);
	}

	@Context
	private UriInfo uriInfo;

	@Inject
	private KamerRepository kamerRepository;

	/**
	 * Haalt de WebSocket URL op voor de kamer met de code {@code kamerCode}.
	 *
	 * @param kamerCode De code van een kamer.
	 * @return De WebSocket URL van de kamer.
	 */
	public String getUrl(String kamerCode) {
		URI uri = uriInfo.getBaseUri();

		return UriBuilder
			.fromUri(uri)
			.path(getClass().getAnnotation(ServerEndpoint.class).value())
			.scheme("https".equals(uri.getScheme()) ? "wss" : "ws")
			.build(kamerCode).toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@OnOpen
	public void open(Session session, EndpointConfig config, @PathParam("kamerCode") String kamerCode) {
		if (!KamerService.geregistreerdeKamers.contains(kamerCode)) {
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
	public EventResponse message(Event event, @PathParam("kamerCode") String kamerCode, Session session) {
		String eventKamerCode = event.getDeelnemerId().getKamerCode();
		if (!eventKamerCode.equals(kamerCode)) {
			return new EventResponse(EventResponse.Status.VERBODEN);
		}

		Optional<Kamer> kamer = kamerRepository.get(eventKamerCode);
		if (kamer.isPresent()) {
			event.voerUit(kamerRepository, kamer.get(), session);
		} else {
			return new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN);
		}

		return new EventResponse(EventResponse.Status.OK);
	}

	/**
	 * {@inheritDoc}
	 */
	@OnError
	public void error(Session session, Throwable error, @PathParam("kamerCode") String kamerCode) {
		if (!(error instanceof IllegalArgumentException)) {
			KamerService.LOGGER.log(Level.SEVERE, error.getMessage(), error);
		}
	}

}
