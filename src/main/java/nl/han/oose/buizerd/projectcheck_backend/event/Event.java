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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.reflections.Reflections;

/**
 * Representeert een event die een client verstuurt.
 */
public abstract class Event {

	/**
	 * @return de naam van de {@code eventKlasse} in uppercase snake case
	 */
	protected static String getEventNaam(Class<? extends Event> eventKlasse) {
		return eventKlasse.getSimpleName().replace("Event", "").replaceAll("(?<!^)(?=[A-Z])", "_").toUpperCase();
	}

	// Package-private zodat het getest kan worden
	@NotNull
	@Valid
	DeelnemerId deelnemerId;

	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	/**
	 * Voert het event in kwestie uit.
	 */
	public final CompletionStage<EventResponse> voerUit(DAO dao, Deelnemer deelnemer, Session session) {
		return CompletableFuture
			       .supplyAsync(() -> voerUit(deelnemer, session))
			       .thenApply(result -> {
				       handelAf(dao, deelnemer.getKamer());
				       return result.antwoordOp(this);
			       });
	}

	/**
	 * Wordt aangeroepen bij het uitvoeren van een event.
	 */
	@ValidateOnExecution
	protected abstract @NotNull @Valid EventResponse voerUit(Deelnemer deelnemer, Session session);

	/**
	 * Wordt aangeroepen bij het afhandelen van een event.
	 * <p>
	 * Als de staat van {@code kamer} is veranderd, dan moet
	 * dit doorgevoerd worden aan de datastore met behulp
	 * van de {@code dao}.
	 */
	protected void handelAf(DAO dao, Kamer kamer) {
		// Doe niets
	}

	public static final class Decoder implements jakarta.websocket.Decoder.Text<Event> {

		private static final Gson GSON;
		private static final Validator VALIDATOR;

		static {
			var eventRuntimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Event.class, "event");

			var reflections = new Reflections(Event.class.getPackage().getName());
			reflections.getSubTypesOf(Event.class).forEach(
				event -> eventRuntimeTypeAdapterFactory.registerSubtype(event, getEventNaam(event))
			);

			GSON = new GsonBuilder().registerTypeAdapterFactory(eventRuntimeTypeAdapterFactory).create();
			VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
		}

		@Override
		public Event decode(String s) throws DecodeException {
			try {
				var event = GSON.fromJson(s, Event.class);
				if (event == null) {
					throw new DecodeException(s, "Decoded result is null");
				}

				var violations = VALIDATOR.validate(event);
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

		@Override
		public boolean willDecode(String s) {
			return s != null && !s.isEmpty();
		}

	}

}
