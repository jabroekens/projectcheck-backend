package nl.han.oose.buizerd.projectcheck_backend.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import java.util.concurrent.CompletableFuture;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import org.reflections.Reflections;

/**
 * Representeert een event.
 */
public abstract class Event {

	/*
	 * `transient` zodat het niet ge(de)serializeerd wordt door Gson.
	 * package-private zodat het getest kan worden.
	 */
	transient boolean stuurNaarAlleClients = false;

	@NotNull
	@Valid
	private DeelnemerId deelnemerId;

	/**
	 * Haal het {@link DeelnemerId} op van de deelnemer die het event heeft aangeroepen.
	 *
	 * @return De identifier van de betrokken deelnemer.
	 */
	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	/**
	 * Geeft aan dat de {@link EventResponse} die volgt
	 * uit {@link Event#voerUit(Kamer, Session)} naar
	 * alle open sessies gestuurd moet worden.
	 */
	protected void stuurNaarAlleClients() {
		this.stuurNaarAlleClients = true;
	}

	/**
	 * Voert de event in kwestie uit.
	 *
	 * @param kamerRepository Een {@link KamerRepository}.
	 * @param kamer De kamer waarvoor het event aangeroepen wordt.
	 * @param session De betrokken {@link Session}.
	 */
	public final void voerUit(KamerRepository kamerRepository, Kamer kamer, Session session) {
		CompletableFuture.runAsync(() -> {
			String response = voerUit(kamer, session).AntwoordOp(this).asJson();

			if (stuurNaarAlleClients) {
				/*
				 * foreach loop zodat er geen try/catch-statement
				 * nodig is voor `sendText(String)`
				 */
				for (Session s : session.getOpenSessions()) {
					if (s.isOpen()) {
						s.getAsyncRemote().sendText(response);
					}
				}
			} else if (session.isOpen()) {
				session.getAsyncRemote().sendText(response);
			}

			handelAf(kamerRepository, kamer);
		});
	}

	/**
	 * Wordt aangeroepen bij het uitvoeren van een event.
	 *
	 * @param kamer De kamer waarvoor het event aangeroepen wordt.
	 * @param session De betrokken {@link Session}.
	 * @return Een {@link EventResponse}.
	 */
	@ValidateOnExecution
	protected abstract @NotNull @Valid EventResponse voerUit(Kamer kamer, Session session);

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
	protected void handelAf(KamerRepository kamerRepository, Kamer kamer) {
		// Doe niets
	}

	/**
	 * Decodeert een {@link Event}.
	 *
	 * @see jakarta.websocket.Decoder.Text
	 */
	public static final class Decoder implements jakarta.websocket.Decoder.Text<Event> {

		private static final Gson GSON;
		private static final Validator VALIDATOR;

		static {
			RuntimeTypeAdapterFactory<Event> eventAdapterFactory = RuntimeTypeAdapterFactory.of(Event.class, "event");

			Reflections reflections = new Reflections(Event.class.getPackage().getName());
			reflections.getSubTypesOf(Event.class).forEach(
				event -> eventAdapterFactory.registerSubtype(event, Decoder.getEventNaam(event))
			);

			GSON = new GsonBuilder().registerTypeAdapterFactory(eventAdapterFactory).create();
			VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
		}

		protected static String getEventNaam(Class<? extends Event> eventKlasse) {
			return eventKlasse.getSimpleName().replace("Event", "").replaceAll("(?<!^)(?=[A-Z])", "_").toUpperCase();
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
				if (event == null || !Decoder.VALIDATOR.validate(event).isEmpty()) {
					return false;
				}
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
