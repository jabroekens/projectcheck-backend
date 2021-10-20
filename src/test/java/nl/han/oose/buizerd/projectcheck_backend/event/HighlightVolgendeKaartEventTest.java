package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighlightVolgendeKaartEventTest {

	private HighlightVolgendeKaartEvent sut;

	@BeforeEach
	void setUp() {
		sut = new HighlightVolgendeKaartEvent();
	}

	@Test
	void voerUit_deelnemerIsGeenBegeleider_stuurtJuisteEventResponse(@Mock Deelnemer deelnemer) {
		var eventResponse = sut.voerUit(deelnemer);
		assertEquals(EventResponse.Status.VERBODEN, eventResponse.getStatus());
	}

	@Nested
	class voerUit_deelnemerIsBegeleider {

		@Mock
		private Begeleider begeleider;

		@Mock
		private Kamer kamer;

		@BeforeEach
		void setUp() {
			when(begeleider.getKamer()).thenReturn(kamer);
		}

		@Test
		void huidigeRondeAfwezig_stuurtJuisteEventResponse() {
			when(kamer.getHuidigeRonde()).thenReturn(Optional.empty());

			var eventResponse = sut.voerUit(begeleider);

			assertEquals(EventResponse.Status.RONDE_NIET_GEVONDEN, eventResponse.getStatus());
		}

		@Test
		void huidigeRondeAanwezig_stuurtJuisteEventResponse(@Mock Ronde ronde) {
			when(kamer.getHuidigeRonde()).thenReturn(Optional.of(ronde));

			var eventResponse = sut.voerUit(begeleider);

			assertAll(
				() -> verify(ronde).setGehighlighteKaart(null),
				() -> assertNull(eventResponse.getContext().get("gehighlighteKaart")),
				() -> assertTrue(eventResponse.isStuurNaarAlleClients())
			);
		}

	}

}
