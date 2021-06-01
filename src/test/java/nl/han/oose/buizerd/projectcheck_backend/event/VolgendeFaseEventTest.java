package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class VolgendeFaseEventTest {

	private VolgendeFaseEvent volgendeFaseEvent;

	@BeforeEach
	void setUp() {
		volgendeFaseEvent = new VolgendeFaseEvent();
	}

	@Test
	void handelAf_updateKamer(@Mock DAO dao, @Mock Kamer kamer) {
		volgendeFaseEvent.handelAf(dao, kamer);
		Mockito.verify(dao).update(kamer);
	}

	@Nested
	class voerUit {

		@Mock
		private Session session;

		@Test
		void deelnemerIsBegeleider_zetKamerFaseNaarVolgendeFaseEnGeeftJuisteEventResponseTerug(
			@Mock Begeleider begeleider, @Mock Kamer kamer, @Mock KamerFase kamerFase
		) {
			Mockito.when(begeleider.getKamer()).thenReturn(kamer);
			Mockito.when(kamer.getKamerFase()).thenReturn(kamerFase);

			EventResponse eventResponse = volgendeFaseEvent.voerUit(begeleider, session);

			Assertions.assertAll(
				() -> {
					KamerFase expectedKamerFase = Mockito.verify(kamerFase).volgendeFase();
					Mockito.verify(kamer).setKamerFase(expectedKamerFase);
				},
				() -> Assertions.assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
				() -> Assertions.assertEquals(kamer.getKamerFase(), eventResponse.getContext().get("volgendeFase"))
			);
		}

		@Test
		void deelnemerIsNietBegeleider_geeftJuisteEventResponseTerug(@Mock Deelnemer deelnemer) {
			Assertions.assertEquals(EventResponse.Status.VERBODEN, volgendeFaseEvent.voerUit(deelnemer, session).getStatus());
		}

	}

}
