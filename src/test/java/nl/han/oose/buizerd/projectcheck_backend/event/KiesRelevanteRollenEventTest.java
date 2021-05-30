package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.HashSet;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KiesRelevanteRollenEventTest {

	@Spy
	Set<Rol> relevanteRollen;

	private KiesRelevanteRollenEvent kiesRelevanteRollenEvent;

	@BeforeEach
	void setUp() {
		relevanteRollen = new HashSet<>();
		kiesRelevanteRollenEvent = new KiesRelevanteRollenEvent();
		kiesRelevanteRollenEvent.relevanteRollen = relevanteRollen;
	}

	@Test
	void handelAf_updateKamer(@Mock DAO<Kamer, String> kamerDAO, @Mock Kamer kamer) {
		kiesRelevanteRollenEvent.handelAf(kamerDAO, kamer);
		Mockito.verify(kamerDAO).update(kamer);
	}

	@Nested
	class voerUit {

		@Test
		void deelnemerIsBegeleider(@Mock Begeleider begeleider, @Mock Session session, @Mock Kamer kamer) {
			// Arrange
			// Een gemockte kamer heeft geen deelnemers, dus moeten wij deze zelf toevoegen
			kamer.voegDeelnemerToe(begeleider);
			Mockito.when(begeleider.getKamer()).thenReturn(kamer);
			String expectedContextBericht = String.format("de rollen %s zijn ingeschakeld voor de kamer %s", relevanteRollen, begeleider.getKamer().getKamerCode());

			// Act
			EventResponse response = kiesRelevanteRollenEvent.voerUit(begeleider, session);

			// Assert
			Assertions.assertAll(
				() -> Mockito.verify(kamer).activeerRelevanteRollen(relevanteRollen),
				() -> Assertions.assertEquals(EventResponse.Status.OK, response.getStatus()),
				() -> Assertions.assertEquals(expectedContextBericht, response.getContext().get("bericht"))
			);
		}

		@Test
		void deelnemerIsNietBegeleider(@Mock Deelnemer deelnemer, @Mock Session session) {
			// Arrange
			DeelnemerId expectedContextDeelnemer = deelnemer.getDeelnemerId();

			// Act
			EventResponse response = kiesRelevanteRollenEvent.voerUit(deelnemer, session);

			// Assert
			Assertions.assertAll(
				() -> Assertions.assertEquals(EventResponse.Status.VERBODEN, response.getStatus()),
				() -> Assertions.assertEquals(expectedContextDeelnemer, response.getContext().get("deelnemer"))
			);
		}

	}

}
