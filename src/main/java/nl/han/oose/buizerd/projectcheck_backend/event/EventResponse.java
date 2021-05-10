package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.websocket.EndpointConfig;
import java.io.Serializable;
import java.time.LocalDateTime;
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
	@ValidateOnExecution
	public EventResponse(@NotNull EventResponse.Status status) {
		this(status, null);
	}

	/**
	 * Construeert een {@link EventResponse}.
	 *
	 * @param status De responsestatus.
	 * @param context De context van de response.
	 */
	@ValidateOnExecution
	public EventResponse(@NotNull EventResponse.Status status, String context) {
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
	 * @see jakarta.websocket.Encoder.Text
	 */
	public static class Encoder implements jakarta.websocket.Encoder.Text<EventResponse> {

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
