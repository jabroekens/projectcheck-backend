package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventResponseTest {

	@Mock
	private EventResponse.Status status;

	private EventResponse sut;

	@BeforeEach
	void setUp() {
		sut = new EventResponse(status);
	}

	@Test
	void getStatus_geeftJuisteWaarde() {
		assertEquals(status, sut.getStatus());
	}

	@Test
	void getContext_isNietNull() {
		assertNotNull(sut.getContext());
	}

	@Test
	void metContext_voegtAanContextToeEnGeeftZichzelfTerug(@Mock Object object) {
		String waarde = "";

		assertAll(
			() -> assertSame(sut, sut.metContext(waarde, object)),
			() -> assertEquals(object, sut.getContext().get(waarde))
		);
	}

	@Test
	void getDatum_isNietNull() {
		assertNotNull(sut.getDatum());
	}

	@Test
	void antwoordOp_zetEnGeeftJuisteWaarde(@Mock Event event) {
		String antwoordOp = "";

		try (MockedStatic<Event> mock = mockStatic(Event.class)) {
			mock.when(() -> Event.getEventNaam(event.getClass())).thenReturn(antwoordOp);

			EventResponse actualEventResponse = sut.antwoordOp(event);

			assertAll(
				() -> mock.verify(() -> Event.getEventNaam(event.getClass())),
				() -> assertSame(sut, actualEventResponse),
				() -> assertEquals(antwoordOp, sut.getAntwoordOp())
			);
		}
	}

	@Test
	void isStuurNaarAlleClients_isStandaardFalse() {
		assertFalse(sut.isStuurNaarAlleClients());
	}

	@Test
	void stuurNaarAlleClients_zetWaardeOpTrueEnGeeftZichzelfTerug() {
		assertAll(
			() -> assertSame(sut, sut.stuurNaarAlleClients()),
			() -> assertTrue(sut.isStuurNaarAlleClients())
		);
	}

	@Test
	void asJson_isNietNull() {
		assertNotNull(sut.asJson());
	}

}
