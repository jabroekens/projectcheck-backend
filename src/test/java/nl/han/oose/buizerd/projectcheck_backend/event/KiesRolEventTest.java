package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KiesRolEventTest {

	@Mock
	private Rol rol;

	private KiesRolEvent sut;

	@BeforeEach
	void setUp() {
		sut = new KiesRolEvent();
		sut.rol = rol;
	}

	@Test
	void voerUit_deelnemerKrijgtRol(@Mock Deelnemer deelnemer, @Mock Session session) {
		var eventResponse = sut.voerUit(deelnemer, session);

		assertAll(
			() -> verify(deelnemer).setRol(rol),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertEquals(rol, eventResponse.getContext().get("gekozenRol"))
		);
	}

}
