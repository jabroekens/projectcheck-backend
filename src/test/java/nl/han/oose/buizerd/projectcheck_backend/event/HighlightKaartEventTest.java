package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.websocket.Session;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighlightKaartEventTest {

	@Mock
	private KaartToelichting kaartToelichting;

	private HighlightenKaartEvent sut;

	@BeforeEach
	void setUp() {
		sut = new HighlightenKaartEvent();
		sut.kaartToelichting = kaartToelichting;
	}

	@Test
	void voerUit_huidigeRondeAfwezig_stuurtJuisteEventResponse(@Mock Deelnemer deelnemer, @Mock Session session, @Mock Kamer kamer) {
		when(deelnemer.getKamer()).thenReturn(kamer);
		when(kamer.getHuidigeRonde()).thenReturn(Optional.empty());

		var eventResponse = sut.voerUit(deelnemer, session);

		assertEquals(EventResponse.Status.RONDE_NIET_GEVONDEN, eventResponse.getStatus());
	}

	@Nested
	class voerUit_huidigeRondeAanwezig {

		@Mock
		private Deelnemer deelnemer;

		@Mock
		private Session session;

		@Mock
		private Ronde ronde;

		@BeforeEach
		void setUp(@Mock Kamer kamer) {
			when(deelnemer.getKamer()).thenReturn(kamer);
			when(kamer.getHuidigeRonde()).thenReturn(Optional.of(ronde));
		}

		@Test
		void magNietHighlighten_geeftJuisteEventResponse(@Mock KaartToelichting kaartToelichting) {
			when(ronde.getGehighlighteKaart()).thenReturn(Optional.of(kaartToelichting));

			var eventResponse = sut.voerUit(deelnemer, session);

			assertEquals(EventResponse.Status.VERBODEN, eventResponse.getStatus());
		}

		@Test
		void magHighlighten_zetGehighlighteKaart() {
			when(ronde.getGehighlighteKaart()).thenReturn(Optional.empty());

			var eventResponse = sut.voerUit(deelnemer, session);

			assertAll(
				() -> verify(ronde).setGehighlighteKaart(kaartToelichting),
				() -> assertEquals(kaartToelichting, eventResponse.getContext().get("gehighlighteKaart")),
				() -> assertTrue(eventResponse.isStuurNaarAlleClients()),
				() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
			);
		}

	}

}
