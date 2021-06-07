package nl.han.oose.buizerd.projectcheck_backend.event;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.executable.ValidateOnExecution;
import jakarta.websocket.DecodeException;
import jakarta.websocket.Session;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
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
	 * Voert het event in kwestie uit.
	 *
	 * @param dao Een {@link DAO}.
	 * @param deelnemer De {@link Deelnemer} die het event heeft aangeroepen.
	 * @param session De betrokken {@link Session}.
	 */
	public final CompletionStage<EventResponse> voerUit(DAO dao, Deelnemer deelnemer, Session session) {
		return CompletableFuture
			.supplyAsync(() -> voerUit(deelnemer, session))
			.thenApply((result) -> {
				handelAf(dao, deelnemer.getKamer());
				return result;
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
	 * @param dao Een {@link DAO}.
	 * @param kamer De kamer waaraan de deelnemer die het event heeft aangeroepen deelneemt.
	 */
	protected void handelAf(DAO dao, Kamer kamer) {
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
				event -> eventAdapterFactory.registerSubtype(event, getEventNaam(event))
			);

			GSON = new GsonBuilder().registerTypeAdapterFactory(eventAdapterFactory).create();
			VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Event decode(String s) throws DecodeException {
			try {
				Event event = GSON.fromJson(s, Event.class);
				if (event == null) {
					throw new DecodeException(s, "Decoded result is null");
				}

				Set<ConstraintViolation<Event>> violations = VALIDATOR.validate(event);
				if (!violations.isEmpty()) {
					throw new DecodeException(
						s,
						String.format(
							"Decoded event has constraint violations: %s",
							violations.stream().map(ConstraintViolation::getMessage).toArray()
						)
					);
				}

				return event;
			} catch (JsonParseException e) {
				throw new DecodeException(s, e.getMessage(), e);
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean willDecode(String s) {
			return s != null && !s.isEmpty();
		}

	}

}
