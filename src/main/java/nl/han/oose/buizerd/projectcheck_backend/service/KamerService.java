package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class KamerService extends WebSocketServer {

	private final Kamer kamer;

	private final Gson gson;
	private final Map<String, WebSocketService> services;

	public KamerService(@NotNull Kamer kamer) {
		this.kamer = kamer;
		this.gson = new Gson();
		this.services = Collections.synchronizedMap(new HashMap<>());

		handle(kamer, (conn, message) -> {
			try {
				// TODO vertaal message naar een soort event en voort dit uit
			} catch (JsonSyntaxException e) {
				// TODO return foutmelding
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
		String path = URI.create(conn.getResourceDescriptor()).getPath();
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
