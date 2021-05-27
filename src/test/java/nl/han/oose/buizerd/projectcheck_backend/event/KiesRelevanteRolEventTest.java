package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KiesRelevanteRolEventTest {

	private KiesRelevanteRolEvent kiesRelevanteRolEvent;
	private Session session;
	private Kamer kamer;
	private Begeleider begeleider;
	private DeelnemerId begeleiderId;
	private Set<Rol> rollen;

	@BeforeEach
	void setUp() {
		session = Mockito.mock(Session.class);
		kamer = Mockito.mock(Kamer.class);
		begeleider = Mockito.mock(Begeleider.class);
		begeleiderId = Mockito.mock(DeelnemerId.class);
		kiesRelevanteRolEvent = new KiesRelevanteRolEvent();
		kiesRelevanteRolEvent.rollen = rollen;
		kiesRelevanteRolEvent.deelnemerId = Mockito.mock(DeelnemerId.class);
	}

	@Test
	public void voerUit_RolBestaand_VerbodenDeelnemer() {
		// Arrange
		Mockito.when(kamer.getBegeleider()).thenReturn(begeleider);
		Mockito.when(begeleider.getDeelnemerId()).thenReturn(begeleiderId);
		String expectedBericht = String.format("de rollen %s zijn ingeschakeld voor de kamer %s", rollen, kamer.getKamerCode());

		// Act
		EventResponse response = kiesRelevanteRolEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(kamer).getBegeleider(),
			() -> Mockito.verify(begeleider).getDeelnemerId(),
			() -> Assertions.assertEquals(EventResponse.Status.VERBODEN, response.status),
			() -> Assertions.assertEquals(response.context.get("deelnemer"), kiesRelevanteRolEvent.getDeelnemerId())
		);

	}

	@Test
	public void voerUit_RolBestaand_DeelnemerIsBegeleider() {
		// Arrange
		Mockito.when(kamer.getBegeleider()).thenReturn(begeleider);
		Mockito.when(begeleider.getDeelnemerId()).thenReturn(kiesRelevanteRolEvent.getDeelnemerId());
		String expectedBericht = String.format("de rollen %s zijn ingeschakeld voor de kamer %s", rollen, kamer.getKamerCode());

		// Act
		EventResponse response = kiesRelevanteRolEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(kamer).getBegeleider(),
			() -> Mockito.verify(begeleider).getDeelnemerId(),
			() -> Assertions.assertEquals(EventResponse.Status.OK, response.status),
			() -> Assertions.assertEquals(expectedBericht, response.context.get("bericht"))
		);

	}

	// @Test
	// public void voerUit_RolNietBestaand() {
	// 	// Arrange
	// 	try (MockedStatic<Rol> mock = Mockito.mockStatic(Rol.class)) {
	// 		mock.when(() -> Rol.valueOf(rolNaam)).thenThrow(new IllegalArgumentException());
	//
	// 		// Act
	// 		EventResponse response = kiesRelevanteRolEvent.voerUit(kamer, session);
	//
	// 		// Assert
	// 		Assertions.assertAll(
	// 			() -> Assertions.assertEquals(EventResponse.Status.ROL_NIET_GEVONDEN, response.status),
	// 			() -> Assertions.assertEquals(rollen, response.context.get("rollen"))
	// 		);
	// 	}
	//
	// }

}
