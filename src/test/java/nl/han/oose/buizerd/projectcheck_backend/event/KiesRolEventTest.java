package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KiesRolEventTest {

	private KiesRolEvent kiesRolEvent;
	private Session session;
	private Kamer kamer;
	private Deelnemer deelnemer;
	private Rol rol;

	@BeforeEach
	void setUp() {
		session = Mockito.mock(Session.class);
		kamer = Mockito.mock(Kamer.class);
		deelnemer = Mockito.mock(Deelnemer.class);
		kiesRolEvent = new KiesRolEvent();
		rol = Mockito.mock(Rol.class);
		kiesRolEvent.rol = rol;

	}

	// ErrorPath
	@Test
	public void voerUit_kanDeelnemerNietvindenTest() {
		// Arrange
		// De deelnemer is wel gemocked in de setUp() maar nog niet toegevoegd aan de kamer. Daarom zal de KiesRolEvent geen deelnemer vinden.

		// Act
		EventResponse response = kiesRolEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Assertions.assertEquals(EventResponse.Status.DEELNEMER_NIET_GEVONDEN, response.status)
		);
	}

	// HappyPath
	@Test
	public void voerUit_deelnemerKrijgtRol(){
		// Arrange
		Rol expectedRol = rol;
		Mockito.when(kamer.getDeelnemer(kiesRolEvent.getDeelnemerId())).thenReturn(Optional.of(deelnemer));

		// Act
		EventResponse response = kiesRolEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Assertions.assertEquals(EventResponse.Status.OK, response.status),
			() -> Assertions.assertEquals(expectedRol, response.context.get("gekozenRol"))
		);
	}

}
