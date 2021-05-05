package nl.han.oose.buizerd.projectcheck_backend.service;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

@ServerEndpoint("/kamer/{kamerCode}")
public class KamerService {

	private static final Map<String, WebSocketService> services;

	static {
		services = new ConcurrentHashMap<>();
	}

	/**
	 * Registreert een {@link Kamer} en stelt een WebSocket URL beschikbaar.
	 *
	 * @param kamerCode De code van een kamer.
	 * @see KamerService#getUrl(String)
	 */
	public static void registreer(@NotNull String kamerCode) {
		KamerService.services.put(kamerCode, (session, message) -> {
			// try {
			// 	/*
			// 	 * FIXME vertaal message naar een soort event en voort dit uit
			// 	 *  * https://stackoverflow.com/a/15593399
			// 	 *  * https://github.com/google/gson/blob/master/UserGuide.md#serializing-and-deserializing-collection-with-objects-of-arbitrary-types
			// 	 */
			// 	Event event = Util.getGson().fromJson(message, Event.class);
			//
			// 	String eventKamerCode = event.getDeelnemerId().getKamerCode();
			// 	Optional<Kamer> kamer = kamerRepository.get(eventKamerCode);
			//
			// 	if (kamer.isPresent()) {
			// 		event.verwerkEvent(kamerRepository, kamer.get(), session);
			// 	} else {
			// 		// TODO betere foutafhandeling (misschien een klasse speciaal voor gebruikersvriendelijke foutmeldingen?)
			// 		session.getBasicRemote().sendText(Util.getGson().toJson(new KamerNietGevondenException(eventKamerCode)));
			// 	}
			// } catch (JsonSyntaxException e) {
			// 	session.getBasicRemote().sendText(Util.getGson().toJson(new InvalideWebSocketEventException()));
			// }
		});
	}

	private static void delegeer(String kamerCode, Session session, String message) throws IOException {
		WebSocketService service = services.get(kamerCode);

		if (service != null) {
			service.onMessage(session, message);
		}
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
		if (!KamerService.services.containsKey(kamerCode)) {
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
	public void message(String message, @PathParam("kamerCode") String kamerCode, Session session) {
		try {
			KamerService.delegeer(kamerCode, session, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnError
	public void error(Session session, Throwable error, @PathParam("kamerCode") String kamerCode) {
		error.printStackTrace();
	}

}
