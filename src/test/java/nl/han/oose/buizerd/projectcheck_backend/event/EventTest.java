package nl.han.oose.buizerd.projectcheck_backend.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EventTest {

	private Event event;

	@BeforeEach
	void init() {
		event = Mockito.mock(Event.class, Mockito.withSettings().defaultAnswer(Mockito.CALLS_REAL_METHODS));
	}

	@Test
	void geeftEventType() {
		Assertions.assertNull(event.getEventType());
	}

	@Test
	void geeftDeelnemerId() {
		Assertions.assertNull(event.getDeelnemerId());
	}

	@Test
	void geeftContext() {
		Assertions.assertNull(event.getContext());
	}

}
