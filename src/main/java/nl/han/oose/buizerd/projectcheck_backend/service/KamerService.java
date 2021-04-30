package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class KamerService extends WebSocketServer {

	private final Kamer kamer;

	private final Gson gson;
	private final Map<String, WebSocketService> services;

	public KamerService(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer) {
		this.kamer = kamer;

		this.gson = new Gson();
		this.services = Collections.synchronizedMap(new HashMap<>());

		handle(kamer, (conn, message) -> {
			try {
				 /*
				  * FIXME vertaal message naar een soort event en voort dit uit
				 *  * https://stackoverflow.com/a/15593399
				 *  * https://github.com/google/gson/blob/master/UserGuide.md#serializing-and-deserializing-collection-with-objects-of-arbitrary-types
				 */
				// Event event = gson.fromJson(message, VolgendeRondeEvent.class);
			} catch (JsonSyntaxException e) {
				conn.send(gson.toJson(Response.status(Response.Status.BAD_REQUEST)));
			}
		});
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conn.setAttachment(kamer);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {

	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		String path = URI.create(conn.getResourceDescriptor()).getPath().replace("/projectcheck-backend-0.0.1", "");
		WebSocketService service = services.get(path);

		if (service != null) {
			service.onMessage(conn, message);
		}
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
	}

	@Override
	public void onStart() {

	}

	private void handle(@NotNull Kamer kamer, WebSocketService service) {
		services.put("/kamer/" + kamer.getKamerCode(), service);
	}

}
