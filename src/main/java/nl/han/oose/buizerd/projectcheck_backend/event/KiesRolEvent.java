package nl.han.oose.buizerd.projectcheck_backend.event;

import java.io.Serializable;
import java.util.Map;
import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.java_websocket.WebSocket;

public class KiesRolEvent extends Event {

	public KiesRolEvent(
		@NotNull EventType eventType,
		@NotNull DeelnemerId deelnemerId,
		Map<String, ? extends Serializable> context
	) {
		super(eventType, deelnemerId, context);
	}

	@Override
	protected void voerUit(@NotNull Kamer kamer, WebSocket conn, @NotNull Rol rol) {
		deelnemerId.getDeelnemer()
				   deelnemer.setRol()
	}

}
