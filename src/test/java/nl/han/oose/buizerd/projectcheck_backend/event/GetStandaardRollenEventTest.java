package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
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
	void voerUit_geeftJuisteEventResponseTerug(@Mock KamerService kamerService, @Mock Set<Rol> standaardRollen) {
		when(kamerService.getStandaardRollen()).thenReturn(standaardRollen);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).getStandaardRollen(),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertTrue(eventResponse.getContext().get("standaardRollen") instanceof Iterable<?>),
			() -> assertIterableEquals(standaardRollen, (Iterable<?>) eventResponse.getContext().get("standaardRollen"))
		);
	}

}
