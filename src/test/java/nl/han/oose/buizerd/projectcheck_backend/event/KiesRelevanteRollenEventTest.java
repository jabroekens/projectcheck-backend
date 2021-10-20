package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
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
	private Set<Rol> relevanteRollen = new HashSet<>();

	private KiesRelevanteRollenEvent sut;

	@BeforeEach
	void setUp() {
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

		@Test
		void deelnemerIsBegeleider_activeertRelevanteRollenEnGeeftJuisteEventResponseTerug(
			@Mock Begeleider begeleider, @Mock Kamer kamer
		) {
			when(begeleider.getKamer()).thenReturn(kamer);

			var eventResponse = sut.voerUit(begeleider);

			assertAll(
				() -> verify(kamer).setRelevanteRollen(relevanteRollen),
				() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
			);
		}

		@Test
		void deelnemerIsNietBegeleider_geeftJuisteEventResponseTerug(@Mock Deelnemer deelnemer) {
			var expected = deelnemer.getDeelnemerId();

			var eventResponse = sut.voerUit(deelnemer);

			assertAll(
				() -> assertEquals(EventResponse.Status.VERBODEN, eventResponse.getStatus()),
				() -> assertEquals(expected, eventResponse.getContext().get("deelnemer"))
			);
		}

	}

}
