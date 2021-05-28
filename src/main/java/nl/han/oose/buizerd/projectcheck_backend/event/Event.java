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
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.reflections.Reflections;

/**
 * Representeert een event.
 */
public abstract class Event {

	/**
	 * Geeft de naam van het event dat volgt uit een eventklasse.
	 *
	 * @return De naam van de {@code eventKlasse} in uppercase snake case.
	 */
	protected static String getEventNaam(Class<? extends Event> eventKlasse) {
		return eventKlasse.getSimpleName().replace("Event", "").replaceAll("(?<!^)(?=[A-Z])", "_").toUpperCase();
	}

	/*
	 * `transient` zodat het niet ge(de)serializeerd wordt door Gson.
	 * package-private zodat het getest kan worden.
	 */
	transient boolean stuurNaarAlleClients = false;

	// package-private zodat het getest kan worden.
	@NotNull
	@Valid
	DeelnemerId deelnemerId;

	/**
	 * Haal het {@link DeelnemerId} op van de deelnemer die het event heeft aangeroepen.
	 */
	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	/**
	 * Geeft aan dat de {@link EventResponse} die volgt uit
	 * {@link Event#voerUit(Deelnemer, Session)} naar alle
	 * open sessies gestuurt moet worden.
	 */
	protected void stuurNaarAlleClients() {
		this.stuurNaarAlleClients = true;
	}

	/**
	 * Voert het event in kwestie uit.
	 *
	 * @param kamerDAO Een {@link DAO} voor {@link Kamer}.
	 * @param deelnemer De {@link Deelnemer} die het event heeft aangeroepen.
	 * @param session De betrokken {@link Session}.
	 */
	public final void voerUit(DAO<Kamer, String> kamerDAO, Deelnemer deelnemer, Session session) {
		CompletableFuture.runAsync(() -> {
			String response = voerUit(deelnemer, session).antwoordOp(this).asJson();

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

			handelAf(kamerDAO, deelnemer.getKamer());
		});
	}

	/**
	 * Wordt aangeroepen bij het uitvoeren van een event.
	 *
	 * @param deelnemer De {@link Deelnemer} die het event heeft aangeroepen.
	 * @param session De betrokken {@link Session}.
	 */
	@ValidateOnExecution
	protected abstract @NotNull @Valid EventResponse voerUit(Deelnemer deelnemer, Session session);

	/**
	 * Wordt aangeroepen bij het afhandelen van een event.
	 * <p>
	 * Deze methode is bedoeld voor het opslaan van veranderingen aan
	 * de datastore. Als de staat van de {@code kamer} is veranderd,
	 * dan moet dit opgeslagen worden met de {@code kamerRepository}.
	 *
	 * @param kamerDAO Een {@link DAO} voor {@link Kamer}.
	 * @param kamer De kamer waaraan de deelnemer die het event heeft aangeroepen deelneemt.
	 */
	protected void handelAf(DAO<Kamer, String> kamerDAO, Kamer kamer) {
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
				event -> eventAdapterFactory.registerSubtype(event, Event.getEventNaam(event))
			);

			GSON = new GsonBuilder().registerTypeAdapterFactory(eventAdapterFactory).create();
			VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
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
