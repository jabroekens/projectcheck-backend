package nl.han.oose.buizerd.projectcheck_backend.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.websocket.EndpointConfig;
import nl.han.oose.buizerd.projectcheck_backend.Util;

/**
 * Representeert een antwoord op een WebSocket bericht.
 */
public class EventResponse implements Serializable {

	private final LocalDateTime datum;
	private final EventResponse.Status status;
	private final String context;

	/**
	 * Construeert een {@link EventResponse} zonder context.
	 *
	 * @param status De responsestatus.
	 */
	public EventResponse(EventResponse.Status status) {
		this(status, null);
	}

	/**
	 * Construeert een {@link EventResponse}.
	 *
	 * @param status De responsestatus.
	 * @param context De context van de response.
	 */
	public EventResponse(EventResponse.Status status, String context) {
		this.datum = LocalDateTime.now();
		this.status = status;
		this.context = context;
	}

	/**
	 * Representeert de status van een {@link EventResponse}.
	 */
	public enum Status {
		OK,
		VERBODEN,
		INVALIDE,
		KAMER_NIET_GEVONDEN,
	}

	/**
	 * Encodeert een {@link EventResponse}.
	 *
	 * @see javax.websocket.Encoder.Text
	 */
	public static class Encoder implements javax.websocket.Encoder.Text<EventResponse> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public String encode(EventResponse object) {
			return Util.getGson().toJson(object);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void init(EndpointConfig config) {
			// Wordt niet gebruikt
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void destroy() {
			// Wordt niet gebruikt
		}

	}

}
