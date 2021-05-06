package nl.han.oose.buizerd.projectcheck_backend.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.validation.constraints.NotNull;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

// TODO Unit tests schrijven voor subklassen?
// TODO Code splitsen in aparte bestanden? is dat beter/logischer?
public abstract class AbstractEvent {

	private static final EventDeserializer eventDeserializer;
	private static final Gson gson;

	static {
		eventDeserializer = new EventDeserializer();
		gson = new GsonBuilder().registerTypeAdapter(AbstractEvent.class, eventDeserializer).create();
	}

	protected static void registreerEvent(@NotNull Class<? extends AbstractEvent> eventKlasse) {
		// TODO Klasse registreren (en naam van event bepalen) d.m.v @Event("<event_naam>")
		// https://stackoverflow.com/a/2206432
		eventDeserializer.registreerKlasse(eventKlasse.getSimpleName().toUpperCase(), eventKlasse);
	}

	private DeelnemerId deelnemerId;

	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	public void verwerkEvent(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer, Session session) {
		CompletableFuture
			.runAsync(() -> voerUit(kamer, session))
			.thenRunAsync(() -> verwerk(kamerRepository, kamer));
	}

	protected abstract void voerUit(@NotNull Kamer kamer, Session session);

	protected void verwerk(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer) {
		// Doe niets
	}

	public static class Decoder implements javax.websocket.Decoder.Text<AbstractEvent> {

		@Override
		public AbstractEvent decode(String s) {
			return gson.fromJson(s, AbstractEvent.class);
		}

		@Override
		public boolean willDecode(String s) {
			return s != null && !s.isEmpty();
		}

		@Override
		public void init(EndpointConfig config) {
		}

		@Override
		public void destroy() {
		}

	}

	// https://stackoverflow.com/a/15593399
	private static class EventDeserializer implements JsonDeserializer<AbstractEvent> {

		private final Map<String, Class<? extends AbstractEvent>> eventKlassen;

		private EventDeserializer() {
			eventKlassen = new HashMap<>();
		}

		private void registreerKlasse(@NotNull String eventNaam, @NotNull Class<? extends AbstractEvent> eventKlasse) {
			this.eventKlassen.put(eventNaam, eventKlasse);
		}

		@Override
		public AbstractEvent deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
		throws JsonParseException {
			JsonObject jsonObject = json.getAsJsonObject();
			if (jsonObject.has("eventNaam")) {
				String eventNaam = jsonObject.get("eventNaam").getAsString();

				if (eventKlassen.containsKey(eventNaam)) {
					return context.deserialize(jsonObject, eventKlassen.get(eventNaam));
				} else {
					// XXX Hier een aparte exception-klasse voor aanmaken? Denk ook aan de front-end!
					throw new RuntimeException("Value of 'eventNaam' is not recognized.");
				}
			} else {
				// XXX Is dit de juiste manier om JsonParseException te gebruiken? Zie ook comment hierboven
				throw new JsonParseException("Key 'eventNaam' not present.");
			}
		}

	}

}
