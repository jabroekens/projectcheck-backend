package nl.han.oose.buizerd.projectcheck_backend.event;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.validation.constraints.NotNull;
import javax.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

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

	public void verwerkEvent(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer, Session session) {
		CompletableFuture
			.runAsync(() -> voerUit(kamer, session))
			.thenRunAsync(() -> verwerk(kamerRepository, kamer));
	}

	protected abstract void voerUit(@NotNull Kamer kamer, Session session);

	protected void verwerk(@NotNull KamerRepository kamerRepository, @NotNull Kamer kamer) {
		// Doe niets
	}

	public enum EventType {
	}

}
