package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KiesRelevanteRolEventTest {

	private final static String rolNaam = "OPDRACHTGEVER";
	private KiesRelevanteRolEvent kiesRelevanteRolEvent;
	private Session session;
	private Kamer kamer;
	private Begeleider begeleider;
	private DeelnemerId begeleiderId;
	private DeelnemerId deelnemerId;
	private Rol rol;

	@BeforeEach
	void setUp() {
		session = Mockito.mock(Session.class);
		kamer = Mockito.mock(Kamer.class);
		begeleider = Mockito.mock(Begeleider.class);
		begeleiderId = Mockito.mock(DeelnemerId.class);
		deelnemerId = Mockito.mock(DeelnemerId.class);
		kiesRelevanteRolEvent = new KiesRelevanteRolEvent();
		kiesRelevanteRolEvent.rolNaam = rolNaam;
		kiesRelevanteRolEvent.deelnemerId = deelnemerId;
	}

	@Test
	public void voerUit_RolBestaand_VerbodenDeelnemer() {
		// Arrange
		Mockito.when(kamer.getBegeleider()).thenReturn(begeleider);
		Mockito.when(begeleider.getDeelnemerId()).thenReturn(begeleiderId);
		String expectedBericht = String.format("de rol %s is ingeschakeld voor de kamer %s", rolNaam, kamer.getKamerCode());

		// Act
		EventResponse response = kiesRelevanteRolEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(kamer).getBegeleider(),
			() -> Mockito.verify(begeleider).getDeelnemerId(),
			() -> Assertions.assertEquals(EventResponse.Status.VERBODEN, response.status),
			() -> Assertions.assertEquals(deelnemerId, response.context.get("deelnemer"))
		);

	}

	@Test
	public void voerUit_RolBestaand_DeelnemerIsBegeleider() {
		// Arrange
		Mockito.when(kamer.getBegeleider()).thenReturn(begeleider);
		Mockito.when(begeleider.getDeelnemerId()).thenReturn(deelnemerId);
		String expectedBericht = String.format("de rol %s is ingeschakeld voor de kamer %s", rolNaam, kamer.getKamerCode());

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

	@Test
	public void voerUit_RolNietBestaand() {
		// Arrange
		try (MockedStatic<Rol> mock = Mockito.mockStatic(Rol.class)) {
			mock.when(() -> Rol.valueOf(rolNaam)).thenThrow(new IllegalArgumentException());

			// Act
			EventResponse response = kiesRelevanteRolEvent.voerUit(kamer, session);

			// Assert
			Assertions.assertAll(
				() -> mock.verify(() -> Rol.valueOf(rolNaam)),
				() -> Assertions.assertEquals(EventResponse.Status.ROL_NIET_GEVONDEN, response.status),
				() -> Assertions.assertEquals(rolNaam, response.context.get("rolNaam"))
			);
		}

	}

}
