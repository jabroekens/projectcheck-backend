package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GetStandaardRollenEventTest {

	private GetStandaardRollenEvent sut;

	@BeforeEach
	void setUp() {
		sut = new GetStandaardRollenEvent();
	}

	@Test
	void voerUit_geeftJuisteEventResponseTerug(@Mock Deelnemer deelnemer, @Mock Session session) {
		var eventResponse = sut.voerUit(deelnemer, session);

		assertAll(
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertNotNull(eventResponse.getContext().get("standaardRollen"))
		);
	}

}
