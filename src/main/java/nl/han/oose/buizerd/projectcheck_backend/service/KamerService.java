package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.JsonSyntaxException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.Util;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.exception.InvalideWebSocketEventException;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class KamerService extends WebSocketServer {

	// Vergelijkbaar met JAX-RS's @Path
	private static final String PATH = "/kamer/{kamerCode}";

	private static String getPath(@NotNull String kamerCode) {
		return PATH.replace("{kamerCode}", kamerCode);
	}

	//private final String kamerCode;
	private final KamerRepository kamerRepository;
	private final Map<String, WebSocketService> services;

	/**
	 * Construeert een {@link KamerService}.
	 *
	 * @param kamerRepository Een {@link KamerRepository}.
	 */
	public KamerService(@NotNull KamerRepository kamerRepository) {
		this.kamerRepository = kamerRepository;
		this.services = Collections.synchronizedMap(new HashMap<>());
	}

	/**
	 * Haalt de WebSocket URL op voor de kamer met de code {@code kamerCode}.
	 *
	 * @param kamerCode De code van een kamer.
	 * @return De WebSocket URL van de kamer.
	 */
	public String getUrl(@NotNull String kamerCode) {
		return (getPort() == WebSocketImpl.DEFAULT_WSS_PORT ? "ws://" : "ws://")
			   + getAddress().getHostName() + KamerService.getPath(kamerCode);
	}

	/**
	 * Controleert of een kamer geregistreerd.
	 * <p>
	 * Als een kamer geregistreerd is, dan zal er een WebSocket URL beschikbaar zijn voor deze kamer.
	 *
	 * @param kamerCode De code van een kamer.
	 * @return true als er een kamer met de code {@code kamerCode} is geregistreerd.
	 * @see nl.han.oose.buizerd.projectcheck_backend.service.KamerService#registreer(String)
	 */
	public boolean isGeregistreerd(@NotNull String kamerCode) {
		return services.containsKey(kamerCode);
	}

	/**
	 * Registreert een {@link Kamer} en stelt een WebSocket URL beschikbaar.
	 *
	 * @param kamerCode De code van een kamer.
	 * @see nl.han.oose.buizerd.projectcheck_backend.service.KamerService#getUrl(String)
	 */
	public void registreer(@NotNull String kamerCode) {
		services.put(KamerService.getPath(kamerCode), (conn, message) -> {
			try {
				/*
				 * FIXME vertaal message naar een soort event en voort dit uit
				 *  * https://stackoverflow.com/a/15593399
				 *  * https://github.com/google/gson/blob/master/UserGuide.md#serializing-and-deserializing-collection-with-objects-of-arbitrary-types
				 */
				// Event event = Util.getGson().fromJson(message, VeranderNaamEvent.class);
				// Kamer kamer = kamerRepository.getKamer(conn.getAttachment());
				//
				// try {
				// 	event.verwerkEvent(kamerRepository, kamer, conn);
				// } catch (KamerNietGevondenException e) {
				// 	// TODO betere foutafhandeling (misschien een klasse speciaal voor gebruikersvriendelijke foutmeldingen?)
				// 	conn.send(Util.getGson().toJson(e));
				// }

			} catch (JsonSyntaxException e) {
				conn.send(Util.getGson().toJson(new InvalideWebSocketEventException()));
			}
		});
	}

	private void delegeer(WebSocket conn, String message) {
		String path = URI.create(conn.getResourceDescriptor()).getPath().replace("/projectcheck-backend-0.0.1", "");
		WebSocketService service = services.get(path);

		if (service != null) {
			service.onMessage(conn, message);
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		String kamerCode = URI.create(conn.getResourceDescriptor()).getPath().replace("/projectcheck-backend-0.0.1/kamer/", "");
		if (services.containsKey(kamerCode)) {
			conn.setAttachment(kamerCode);
			registreer(kamerCode);
		} else {
			// TODO magic variables weghalen
			conn.close(0, "Kamer niet gevonden.");
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		delegeer(conn, message);
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onStart() {

	}

}
