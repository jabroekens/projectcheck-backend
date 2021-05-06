package nl.han.oose.buizerd.projectcheck_backend.service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.event.AbstractEvent;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

@ServerEndpoint(
	value = "/kamer/{kamerCode}",
	decoders = {AbstractEvent.Decoder.class},
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
	public static void registreer(@NotNull String kamerCode) {
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
	public String getUrl(@NotNull String kamerCode) {
		// TODO Schema en pad dynamisch bepalen
		return "ws://" + uriInfo.getBaseUri().getHost() + "/kamer/" + kamerCode;
	}

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

	@OnClose
	public void close(Session session, CloseReason closeReason, @PathParam("kamerCode") String kamerCode) {
		// XXX Behandelen als iemand (onverwachts) een kamer verlaat, bijv.
	}

	@OnMessage
	public EventResponse message(AbstractEvent abstractEvent, @PathParam("kamerCode") String kamerCode, Session session) {
		String eventKamerCode = abstractEvent.getDeelnemerId().getKamerCode();
		Optional<Kamer> kamer = kamerRepository.get(eventKamerCode);

		if (kamer.isPresent()) {
			abstractEvent.verwerkEvent(kamerRepository, kamer.get(), session);
		} else {
			return new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN);
		}

		return new EventResponse(EventResponse.Status.OK);
	}

	@OnError
	public void error(Session session, Throwable error, @PathParam("kamerCode") String kamerCode) {
		error.printStackTrace();
	}

}
