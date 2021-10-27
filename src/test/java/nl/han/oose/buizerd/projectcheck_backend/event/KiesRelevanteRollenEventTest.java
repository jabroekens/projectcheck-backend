package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KiesRelevanteRollenEventTest {

	@Mock
	private Set<Rol> relevanteRollen;

	@Mock
	private KamerService kamerService;

	private KiesRelevanteRollenEvent sut;

	@BeforeEach
	void setUp() {
		sut = new KiesRelevanteRollenEvent();
		sut.relevanteRollen = relevanteRollen;
	}

	@Test
	void voerUit_geeftJuisteEventResponseTerug(@Mock KamerFase volgendeFase) {
		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).kiesRelevanteRollen(sut.getDeelnemerId(), relevanteRollen),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
		);
	}

	@Test
	void voerUit_verbodenToegang_geeftJuisteEventResponse() {
		doThrow(VerbodenToegangException.class)
			.when(kamerService).kiesRelevanteRollen(sut.getDeelnemerId(), relevanteRollen);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).kiesRelevanteRollen(sut.getDeelnemerId(), relevanteRollen),
			() -> assertEquals(EventResponse.Status.VERBODEN, eventResponse.getStatus())
		);
	}

}
