package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
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
	private DeelnemerId deelnemerId;
	private Rol rol;

	@BeforeEach
	void setUp() {
		session = Mockito.mock(Session.class);
		kamer = Mockito.mock(Kamer.class);
		deelnemerId = Mockito.mock(DeelnemerId.class);
		kiesRolEvent = new KiesRolEvent();
		rol = Rol.TEAMLID;
	}

	//ErrorPath
	@Test


}
