package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.HashSet;
import java.util.Set;
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
public class GetRelevanteRollenEventTest {

	private Set<Rol> rollen;
	private GetRelevanteRollenEvent getRelevanteRollenEvent;
	private Session session;
	private Kamer kamer;
	private Deelnemer deelnemer;
	private Rol rol;

	@BeforeEach
	void setUp() {
		rollen = new HashSet<>();
		deelnemer = Mockito.mock(Deelnemer.class);
		session = Mockito.mock(Session.class);
		kamer = Mockito.mock(Kamer.class);
		getRelevanteRollenEvent = new GetRelevanteRollenEvent();
	}

	// HappyPath
	@Test
	public void voerUit_GeeftRelevanteRollenTest() {
		// Arrange
		Mockito.when(deelnemer.getKamer()).thenReturn(kamer);
		Mockito.when(kamer.getRelevanteRollen()).thenReturn(rollen);

		// Act
		EventResponse response = getRelevanteRollenEvent.voerUit(deelnemer, session);

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(kamer).getRelevanteRollen(),
			() -> Assertions.assertEquals(rollen, response.getContext().get("geefRollen")),
			() -> Assertions.assertEquals(EventResponse.Status.OK, response.getStatus())
		);
	}
}
