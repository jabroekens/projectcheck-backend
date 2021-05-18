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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KiesRelevanteRolEventTest {

	private KiesRelevanteRolEvent kiesRelevanteRolEvent;
	private Session session;
	private Kamer kamer;
	private Begeleider begeleider;
	private DeelnemerId begeleiderId;
	private DeelnemerId deelnemerId;
	private final static String rolNaam = "OPDRACHTGEVER";
	private Rol rol;

	@BeforeEach
	void setup() {
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
	public void voerUit_RolBestaand() {
		// Arrange
		Mockito.when(kamer.getBegeleider()).thenReturn(begeleider);
		Mockito.when(begeleider.getDeelnemerId()).thenReturn(begeleiderId);
		//Mockito.when(begeleiderId.equals(deelnemerId)).thenReturn(true);
		String expectedBericht = String.format("de rol %s is ingeschakeld voor de kamer %s", rolNaam, kamer.getKamerCode());

		// Act
		EventResponse response = kiesRelevanteRolEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(kamer).getBegeleider(),
			() -> Assertions.assertEquals(EventResponse.Status.OK, response.status),
			() -> Assertions.assertEquals(expectedBericht, response.context.get("bericht"))
		);

	}

	@Test
	public void voerUit_RolNietBestaand() {
		// Arrange
		Mockito.when(kamer.getBegeleider()).thenReturn(null);

		// Act
		EventResponse response = kiesRelevanteRolEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(kamer).getBegeleider(),
			() -> Assertions.assertEquals(EventResponse.Status.ROL_NIET_GEVONDEN, response.status),
			() -> Assertions.assertEquals(kiesRelevanteRolEvent.getDeelnemerId(), response.context.get("begeleider"))
		);

	}

}
