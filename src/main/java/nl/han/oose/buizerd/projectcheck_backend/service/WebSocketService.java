package nl.han.oose.buizerd.projectcheck_backend.service;

import javax.websocket.Session;

@FunctionalInterface
public interface WebSocketService {

	void onMessage(Session session, String message);

}
