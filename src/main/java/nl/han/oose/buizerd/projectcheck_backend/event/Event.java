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
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

/**
 * Representeert een event.
 */
public abstract class Event {

	private static final EventDeserializer EVENT_DESERIALIZER;
	private static final Gson GSON;

	static {
		EVENT_DESERIALIZER = new EventDeserializer();
		GSON = new GsonBuilder().registerTypeAdapter(Event.class, Event.EVENT_DESERIALIZER).create();
	}

	/**
	 * Registreert een eventklasse.
	 *
	 * @param eventKlasse De eventklasse.
	 */
	protected static void registreerEvent(@NotNull Class<? extends Event> eventKlasse) {
		// Verandert een klassenaam als "FooBarEvent" naar "FOO_BAR"
		String eventNaam = eventKlasse.getSimpleName().replace("Event", "").replaceAll("(?<!^)(?=[A-Z])", "_").toUpperCase();
		Event.EVENT_DESERIALIZER.registreerKlasse(eventNaam, eventKlasse);
	}

	private DeelnemerId deelnemer;

	/**
	 * Haal het {@link Deelnemer} van de deelnemer op die het event heeft aangeroepen.
	 *
	 * @return De identifier van de betrokken deelnemer.
	 */
	public DeelnemerId getDeelnemer() {
		return deelnemer;
	}

	/**
	 * Voert de event in kwestie uit.
	 *
	 * @param kamerRepository Een {@link KamerRepository}.
	 * @param kamer De kamer waarvoor het event aangeroepen wordt.
	 * @param session De betrokken {@link Session}.
	 */
	public void voerUit(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer, Session session) {
		CompletableFuture
			.runAsync(() -> voerUit(kamer, session))
			.thenRunAsync(() -> handelAf(kamerRepository, kamer));
	}

	/**
	 * Wordt aangeroepen bij het uitvoeren van een event.
	 *
	 * @param kamer De kamer waarvoor het event aangeroepen wordt.
	 * @param session De betrokken {@link Session}.
	 */
	protected abstract void voerUit(@NotNull Kamer kamer, Session session);

	/**
	 * Wordt aangeroepen bij het afhandelen van een event.
	 * <p>
	 * Deze methode is bedoeld voor het opslaan van veranderingen aan
	 * de datastore. Als de staat van de {@code kamer} is veranderd,
	 * dan moet dit opgeslagen worden met de {@code kamerRepository}.
	 *
	 * @param kamerRepository Een {@link KamerRepository}.
	 * @param kamer De kamer waarvoor het event aangeroepen wordt.
	 */
	protected void handelAf(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer) {
		// Doe niets
	}

	/**
	 * Decodeert een {@link Event}.
	 *
	 * @see javax.websocket.Decoder.Text
	 */
	public static class Decoder implements javax.websocket.Decoder.Text<Event> {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Event decode(String s) {
			return Event.GSON.fromJson(s, Event.class);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean willDecode(String s) {
			return s != null && !s.isEmpty();
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

	/**
	 * Deserializeert een {@link Event}.
	 *
	 * @see com.google.gson.JsonDeserializer
	 */
	private static class EventDeserializer implements JsonDeserializer<Event> {

		private final Map<String, Class<? extends Event>> eventKlassen;

		private EventDeserializer() {
			eventKlassen = new HashMap<>();
		}

		private void registreerKlasse(@NotNull String eventNaam, @NotNull Class<? extends Event> eventKlasse) {
			this.eventKlassen.put(eventNaam, eventKlasse);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Event deserialize(JsonElement json, java.lang.reflect.Type typeOfT, JsonDeserializationContext context)
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
