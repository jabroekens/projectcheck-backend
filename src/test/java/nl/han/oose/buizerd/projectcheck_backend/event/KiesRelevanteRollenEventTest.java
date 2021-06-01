package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.websocket.Session;
import java.util.HashSet;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KiesRelevanteRollenEventTest {

	@Spy
	Set<Rol> relevanteRollen;

	private KiesRelevanteRollenEvent sut;

	@BeforeEach
	void setUp() {
		relevanteRollen = new HashSet<>();
		sut = new KiesRelevanteRollenEvent();
		sut.relevanteRollen = relevanteRollen;
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
		void deelnemerIsBegeleider_activeertRelevanteRollenEnGeeftJuisteEventResponseTerug(
			@Mock Begeleider begeleider, @Mock Kamer kamer
		) {
			// Arrange
			when(begeleider.getKamer()).thenReturn(kamer);
			String expected = String.format("de rollen %s zijn ingeschakeld voor de kamer %s", relevanteRollen, begeleider.getKamer().getKamerCode());

			// Act
			EventResponse response = sut.voerUit(begeleider, session);

			// Assert
			assertAll(
				() -> verify(kamer).activeerRelevanteRollen(relevanteRollen),
				() -> assertEquals(EventResponse.Status.OK, response.getStatus()),
				() -> assertEquals(expected, response.getContext().get("bericht"))
			);
		}

		@Test
		void deelnemerIsNietBegeleider_geeftJuisteEventResponseTerug(@Mock Deelnemer deelnemer) {
			// Arrange
			DeelnemerId expected = deelnemer.getDeelnemerId();

			// Act
			EventResponse response = sut.voerUit(deelnemer, session);

			// Assert
			assertAll(
				() -> assertEquals(EventResponse.Status.VERBODEN, response.getStatus()),
				() -> assertEquals(expected, response.getContext().get("deelnemer"))
			);
		}

	}

}
