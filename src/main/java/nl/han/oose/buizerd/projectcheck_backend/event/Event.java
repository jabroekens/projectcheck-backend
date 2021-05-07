package nl.han.oose.buizerd.projectcheck_backend.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import java.util.concurrent.CompletableFuture;
import javax.validation.constraints.NotNull;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import org.reflections.Reflections;

/**
 * Representeert een event.
 */
public abstract class Event {

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

		private static final Gson GSON;

		static {
			RuntimeTypeAdapterFactory<Event> eventAdapterFactory = RuntimeTypeAdapterFactory.of(Event.class, "eventNaam");

			Reflections reflections = new Reflections(Event.class.getPackage().getName());
			reflections.getSubTypesOf(Event.class).forEach(
				event -> {
					String eventNaam = event.getSimpleName().replace("Event", "").replaceAll("(?<!^)(?=[A-Z])", "_").toUpperCase();
					eventAdapterFactory.registerSubtype(event, eventNaam);
				}
			);

			GSON = new GsonBuilder().registerTypeAdapterFactory(eventAdapterFactory).create();
		}

		private Event event;

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Event decode(String s) {
			return event;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean willDecode(String s) {
			// Probeer te deserializeren en cache het resultaat als het wel lukt.
			try {
				event = Decoder.GSON.fromJson(s, Event.class);
			} catch (JsonParseException e) {
				return false;
			}
			return true;
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
