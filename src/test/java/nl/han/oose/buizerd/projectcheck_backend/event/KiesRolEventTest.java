package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
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

	// HappyPath
	@Test
	public void voerUit_deelnemerKrijgtRol(){
		// Arrange
		Rol expectedRol = rol;

		// Act
		EventResponse response = kiesRolEvent.voerUit(deelnemer, session);

		// Assert
		Assertions.assertAll(
			() -> Assertions.assertEquals(EventResponse.Status.OK, response.getStatus()),
			() -> Assertions.assertEquals(expectedRol, response.getContext().get("gekozenRol")),
			() -> Mockito.verify(deelnemer).setRol(rol)
		);
	}

}
