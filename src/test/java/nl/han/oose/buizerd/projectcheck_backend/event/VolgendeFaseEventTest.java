package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VolgendeFaseEventTest {

	private VolgendeFaseEvent sut;

	@BeforeEach
	void setUp() {
		sut = new VolgendeFaseEvent();
	}

	@Test
	void handelAf_updateKamer(@Mock DAO dao, @Mock Kamer kamer) {
		sut.handelAf(dao, kamer);
		verify(dao).update(kamer);
	}

	@Nested
	class voerUit {

		@Mock
		private Session session;

		@Test
		void deelnemerIsBegeleider_zetKamerFaseNaarVolgendeFaseEnGeeftJuisteEventResponseTerug(
			@Mock Begeleider begeleider, @Mock Kamer kamer, @Mock KamerFase kamerFase
		) {
			when(begeleider.getKamer()).thenReturn(kamer);
			when(kamer.getKamerFase()).thenReturn(kamerFase);

			var eventResponse = sut.voerUit(begeleider, session);

			assertAll(
				() -> {
					var expectedKamerFase = verify(kamerFase).getVolgendeFase();
					verify(kamer).setKamerFase(expectedKamerFase);
				},
				() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
				() -> assertEquals(kamer.getKamerFase(), eventResponse.getContext().get("volgendeFase"))
			);
		}

		@Test
		void deelnemerIsNietBegeleider_geeftJuisteEventResponseTerug(@Mock Deelnemer deelnemer) {
			assertEquals(EventResponse.Status.VERBODEN, sut.voerUit(deelnemer, session).getStatus());
		}

	}

}
