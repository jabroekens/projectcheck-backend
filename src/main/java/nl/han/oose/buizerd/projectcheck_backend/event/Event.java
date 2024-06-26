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
import jakarta.websocket.EndpointConfig;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
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
	 * Voert het event uit.
	 */
	@ValidateOnExecution
	public abstract @NotNull @Valid EventResponse voerUit(KamerService kamerService);

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

		@Override
		public void init(EndpointConfig config) {
			// Wordt niet gebruikt
		}

		@Override
		public void destroy() {
			// Wordt niet gebruikt
		}

	}

}
