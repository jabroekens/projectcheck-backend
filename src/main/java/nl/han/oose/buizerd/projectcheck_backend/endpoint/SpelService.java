package nl.han.oose.buizerd.projectcheck_backend.endpoint;

import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.Spel;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class SpelService extends WebSocketServer {

	private final Spel spel;

	public SpelService(@NotNull Spel spel) {
		this.spel = spel;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conn.setAttachment(spel);
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
