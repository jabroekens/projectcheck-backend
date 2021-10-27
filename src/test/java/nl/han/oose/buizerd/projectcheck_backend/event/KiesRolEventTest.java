package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
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
	void voerUit_geeftJuisteEventResponseTerug(@Mock KamerService kamerService) {
		var eventResponse = sut.voerUit(kamerService);
		assertAll(
			() -> verify(kamerService).kiesRol(sut.getDeelnemerId(), rol),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertEquals(rol, eventResponse.getContext().get("gekozenRol"))
		);
	}

}
