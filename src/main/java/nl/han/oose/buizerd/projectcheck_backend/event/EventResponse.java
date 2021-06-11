package nl.han.oose.buizerd.projectcheck_backend.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Representeert een antwoord op een {@link Event}.
 */
public class EventResponse {

	private static final Gson GSON;

	static {
		GSON = new GsonBuilder().enableComplexMapKeySerialization()
		                        .excludeFieldsWithoutExposeAnnotation()
		                        .create();
	}

	@Expose
	private final EventResponse.Status status;

	@Expose
	private final Map<String, Object> context;

	@Expose
	private final LocalDateTime datum;

	@Expose
	private String antwoordOp;

	private boolean stuurNaarAlleClients;

	@ValidateOnExecution
	public EventResponse(@NotNull EventResponse.Status status) {
		this.status = status;
		context = new HashMap<>();
		datum = LocalDateTime.now();
	}

	public Status getStatus() {
		return status;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	/**
	 * Voegt {@code waarde} toe aan de context onder {@code sleutel}.
	 *
	 * @return zichzelf
	 */
	public EventResponse metContext(String sleutel, Object waarde) {
		context.put(sleutel, waarde);
		return this;
	}

	public LocalDateTime getDatum() {
		return datum;
	}

	public String getAntwoordOp() {
		return antwoordOp;
	}

	/**
	 * Zet de event waarop antwoord wordt gegeven.
	 *
	 * @return zichzelf
	 */
	public EventResponse antwoordOp(Event event) {
		antwoordOp = Event.getEventNaam(event.getClass());
		return this;
	}

	public boolean isStuurNaarAlleClients() {
		return stuurNaarAlleClients;
	}

	/**
	 * Markeert dat dit {@link EventResponse} naar alle clients gestuurd moet worden.
	 *
	 * @return zichzelf
	 */
	public EventResponse stuurNaarAlleClients() {
		stuurNaarAlleClients = true;
		return this;
	}

	/**
	 * @return een JSON-string representatie van dit {@link EventResponse}
	 */
	public String asJson() {
		return GSON.toJson(this);
	}

	/**
	 * Representeert de status van een {@link EventResponse}.
	 */
	public enum Status {
		OK,
		VERBODEN,
		ONGELDIG,
		KAMER_NIET_GEVONDEN,
		ROL_NIET_GEVONDEN,
		SELECTIE_VOL,
		RONDE_NIET_GEVONDEN
	}

}
