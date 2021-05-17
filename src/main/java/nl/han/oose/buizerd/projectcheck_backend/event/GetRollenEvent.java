package nl.han.oose.buizerd.projectcheck_backend.event;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.Map;
import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.java_websocket.WebSocket;

public class GetRollenEvent extends Event {

	public GetRollenEvent(
		@NotNull EventType eventType,
		@NotNull DeelnemerId deelnemerId,
		Map<String, ? extends Serializable> context
	) {
		super(eventType, deelnemerId, context);
	}

	@Override
	protected void voerUit(@NotNull Kamer kamer, WebSocket conn) {
			Set<Rol> rollen = kamer.getRelevanteRollen();
			Gson gson = new Gson();
			conn.send(gson.toJson(rollen));
	}

}
