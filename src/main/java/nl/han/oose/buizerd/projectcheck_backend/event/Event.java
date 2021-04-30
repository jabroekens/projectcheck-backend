package nl.han.oose.buizerd.projectcheck_backend.event;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import org.java_websocket.WebSocket;

public abstract class Event {

	private final EventType eventType;
	private final DeelnemerId deelnemerId;
	private final Map<String, ? extends Serializable> context;

	public Event(
		@NotNull EventType eventType,
		@NotNull DeelnemerId deelnemerId,
		Map<String, ? extends Serializable> context
	) {
		this.eventType = eventType;
		this.deelnemerId = deelnemerId;
		this.context = context;
	}

	public EventType getEventType() {
		return eventType;
	}

	public DeelnemerId getDeelnemerId() {
		return deelnemerId;
	}

	public Map<String, ? extends Serializable> getContext() {
		return context;
	}

	public void verwerkEvent(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer, WebSocket conn) {
		CompletableFuture
			.runAsync(() -> voerUit(kamer, conn))
			.thenRunAsync(() -> verwerk(kamerRepository, kamer));
	}

	protected abstract void voerUit(@NotNull Kamer kamer, WebSocket conn);

	protected void verwerk(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer) {
		// Doe niets
	}

	public enum EventType {
		VOLGENDE_RONDE(VolgendeRondeEvent.class);

		private final Class<? extends Event> eventKlasse;

		EventType(Class<? extends Event> eventKlasse) {
			this.eventKlasse = eventKlasse;
		}

		public Class<? extends Event> getEventKlasse() {
			return eventKlasse;
		}
	}

}
