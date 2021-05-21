package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.ValidatorFactory;
import jakarta.websocket.Session;
import java.util.HashSet;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GetRelevanteRollenEventTest {

	private Set<Rol> rollen;
	private GetRelevanteRollenEvent getRelevanteRollenEvent;
	private Session session;
	private Kamer kamer;
	private DeelnemerId deelnemerId;
	private Rol rol;

	@BeforeEach
	void setUp() {
		rollen = new HashSet<>();
		rollen.add(Rol.TEAMLID);
		rollen.add(Rol.EINDGEBRUIKER);
		session = Mockito.mock(Session.class);
		kamer = Mockito.mock(Kamer.class);
		deelnemerId = Mockito.mock(DeelnemerId.class);
		getRelevanteRollenEvent = new GetRelevanteRollenEvent();
	}

	//HappyPath
	@Test
	public void voerUit_GeeftRelevanteRollen(@Mock ValidatorFactory validatorFactory) {
		// Arrange
		Mockito.when(kamer.getRelevanteRollen()).thenReturn(rollen);
		String expectedBericht = String.format("geefRollen", rollen);

		// Act
		EventResponse response = getRelevanteRollenEvent.voerUit(kamer, session);

		// Assert
		Assertions.assertAll(
			() -> Mockito.verify(kamer).getRelevanteRollen(),
			() -> Assertions.assertEquals(EventResponse.Status.OK, response.status)
		);
	}

}
