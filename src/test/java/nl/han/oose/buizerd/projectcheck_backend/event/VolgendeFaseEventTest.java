package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VolgendeFaseEventTest {

	private VolgendeFaseEvent sut;

	@Mock
	private KamerService kamerService;

	@BeforeEach
	void setUp() {
		sut = new VolgendeFaseEvent();
	}

	@Test
	void voerUit_geeftJuisteEventResponseTerug(@Mock KamerFase volgendeFase) {
		when(kamerService.naarVolgendeFase(sut.getDeelnemerId())).thenReturn(volgendeFase);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).naarVolgendeFase(sut.getDeelnemerId()),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertEquals(volgendeFase, eventResponse.getContext().get("volgendeFase"))
		);
	}

	@Test
	void voerUit_verbodenToegang_geeftJuisteEventResponse() {
		when(kamerService.naarVolgendeFase(sut.getDeelnemerId())).thenThrow(VerbodenToegangException.class);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).naarVolgendeFase(sut.getDeelnemerId()),
			() -> assertEquals(EventResponse.Status.VERBODEN, eventResponse.getStatus())
		);
	}

}
