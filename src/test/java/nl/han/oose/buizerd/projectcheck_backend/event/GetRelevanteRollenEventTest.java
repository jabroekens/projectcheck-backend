package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.websocket.Session;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetRelevanteRollenEventTest {

	private GetRelevanteRollenEvent sut;

	@BeforeEach
	void setUp() {
		sut = new GetRelevanteRollenEvent();
	}

	@Test
	void voerUit_geeftJuisteEventResponseTerug(
		@Mock Deelnemer deelnemer,
		@Mock Session session,
		@Mock Kamer kamer,
		@Mock Set<Rol> rollen
	) {
		when(deelnemer.getKamer()).thenReturn(kamer);
		when(kamer.getRelevanteRollen()).thenReturn(rollen);

		var eventResponse = sut.voerUit(deelnemer, session);

		assertAll(
			() -> verify(kamer).getRelevanteRollen(),
			() -> assertEquals(rollen, eventResponse.getContext().get("geefRollen")),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
		);
	}

}
