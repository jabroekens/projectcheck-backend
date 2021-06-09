package nl.han.oose.buizerd.projectcheck_backend.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventResponseTest {

	@Mock
	private EventResponse.Status status;

	private EventResponse eventResponse;

	@BeforeEach
	void setUp() {
		eventResponse = new EventResponse(status);
	}

	@Test
	void getStatus_geeftJuisteStatus() {
		Assertions.assertEquals(status, eventResponse.getStatus());
	}

	@Test
	void getContext_contextIsNietNull() {
		Assertions.assertNotNull(eventResponse.getContext());
	}

	@Test
	void metContext_voegtAanContextToeEnGeeftZichzelfTerug(@Mock Object object) {
		String waarde = "";

		Assertions.assertAll(
			() -> Assertions.assertSame(eventResponse, eventResponse.metContext(waarde, object)),
			() -> Assertions.assertEquals(object, eventResponse.getContext().get(waarde))
		);
	}

	@Test
	void getDatum_datumIsNietNull() {
		Assertions.assertNotNull(eventResponse.getDatum());
	}

	@Test
	void getAntwoordOp_geeftJuisteAntwoordOp(@Mock Event event) {
		String antwoordOp = "";

		try (MockedStatic<Event> mock = Mockito.mockStatic(Event.class)) {
			mock.when(() -> Event.getEventNaam(event.getClass())).thenReturn(antwoordOp);

			EventResponse actualEventResponse = eventResponse.antwoordOp(event);

			Assertions.assertAll(
				() -> mock.verify(() -> Event.getEventNaam(event.getClass())),
				() -> Assertions.assertSame(eventResponse, actualEventResponse),
				() -> Assertions.assertEquals(antwoordOp, eventResponse.getAntwoordOp())
			);
		}
	}

	@Test
	void isStuurNaarAlleClients_isStandaardFalse() {
		Assertions.assertFalse(eventResponse.isStuurNaarAlleClients());
	}

	@Test
	void stuurNaarAlleClients_zetWaardeOpTrueEnGeeftZichzelfTerug() {
		Assertions.assertAll(
			() -> Assertions.assertSame(eventResponse, eventResponse.stuurNaarAlleClients()),
			() -> Assertions.assertTrue(eventResponse.isStuurNaarAlleClients())
		);
	}

	@Test
	void asJson_geeftIetsTerug() {
		Assertions.assertNotNull(eventResponse.asJson());
	}

}
