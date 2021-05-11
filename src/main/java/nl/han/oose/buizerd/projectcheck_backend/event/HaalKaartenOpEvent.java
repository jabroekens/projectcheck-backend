package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.io.IOException;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

public class HaalKaartenOpEvent extends Event {

	@Override
	protected void voerUit(Kamer kamer, Session session) throws IOException {
		// TODO : Implement logic.
	}

}
