package nl.han.oose.buizerd.projectcheck_backend.service;

import org.java_websocket.WebSocket;

@FunctionalInterface
public interface WebSocketService {

	void onMessage(WebSocket conn, String message);

}
