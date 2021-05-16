package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import nl.han.oose.buizerd.projectcheck_backend.Util;

/**
 * Representeert een antwoord op een WebSocket bericht.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class EventResponse {

	// package-private zodat het getest kan worden.
	final transient EventResponse.Status status;
	final Map<String, Object> context;

	private final LocalDateTime datum;
	private String antwoordOp;

	/**
	 * Construeert een {@link EventResponse}.
	 *
	 * @param status De responsestatus.
	 */
	@ValidateOnExecution
	public EventResponse(@NotNull EventResponse.Status status) {
		this.status = status;
		this.context = new HashMap<>();
		this.datum = LocalDateTime.now();
	}

	public EventResponse metContext(String sleutel, Object waarde) {
		context.put(sleutel, waarde);
		return this;
	}

	public EventResponse AntwoordOp(Event event) {
		antwoordOp = Event.Decoder.getEventNaam(event.getClass());
		return this;
	}

	public String asJson() {
		return Util.getGson().toJson(this);
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

}
