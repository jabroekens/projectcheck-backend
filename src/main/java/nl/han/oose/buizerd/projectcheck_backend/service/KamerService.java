package nl.han.oose.buizerd.projectcheck_backend.service;

import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class KamerService extends WebSocketServer {

	private final Kamer kamer;

	public KamerService(@NotNull Kamer kamer) {
		this.kamer = kamer;
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

	}

	@Override
	public void onError(WebSocket conn, Exception ex) {

	}

	@Override
	public void onStart() {

	}

}
