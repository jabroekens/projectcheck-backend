package nl.han.oose.buizerd.projectcheck_backend.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.websocket.EndpointConfig;
import nl.han.oose.buizerd.projectcheck_backend.Util;

public class EventResponse implements Serializable {

	private final LocalDateTime datum;
	private final EventResponse.Status status;
	private final String context;

	public EventResponse(EventResponse.Status status) {
		this(status, null);
	}

	public EventResponse(EventResponse.Status status, String context) {
		this.datum = LocalDateTime.now();
		this.status = status;
		this.context = context;
	}

	public enum Status {
		OK,
		KAMER_NIET_GEVONDEN
	}

	public static class Encoder implements javax.websocket.Encoder.Text<EventResponse> {

		@Override
		public String encode(EventResponse object) {
			return Util.getGson().toJson(object);
		}

		@Override
		public void init(EndpointConfig config) {
		}

		@Override
		public void destroy() {
		}

	}

}
