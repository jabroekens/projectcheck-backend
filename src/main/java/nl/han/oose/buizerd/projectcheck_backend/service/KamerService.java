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
import jakarta.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
		// TODO Schema en pad dynamisch bepalen
		return "wss://" + uriInfo.getBaseUri().getHost() + "/kamer/" + kamerCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@OnOpen
	public void open(Session session, EndpointConfig config, @PathParam("kamerCode") String kamerCode) {
		if (!KamerService.geregistreerdeKamers.contains(kamerCode)) {
			try {
				// TODO CloseReason meegeven?
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
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
		String eventKamerCode = event.getDeelnemer().getKamerCode();
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
			error.printStackTrace();
		}
	}

}
