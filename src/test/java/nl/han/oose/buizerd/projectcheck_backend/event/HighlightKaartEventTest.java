package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.exception.RondeNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighlightKaartEventTest {

	@Mock
	private KaartToelichting kaartToelichting;

	private HighlightenKaartEvent sut;

	@BeforeEach
	void setUp() {
		sut = new HighlightenKaartEvent();
		sut.kaartToelichting = kaartToelichting;
	}

	@Test
	void voerUit_rondeNietGevonden_geeftJuisteEventResponseTerug(@Mock KamerService kamerService) {
		doThrow(RondeNietGevondenException.class).when(kamerService).highlightKaart(
			sut.getDeelnemerId(),
			kaartToelichting
		);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).highlightKaart(sut.getDeelnemerId(), kaartToelichting),
			() -> assertEquals(EventResponse.Status.RONDE_NIET_GEVONDEN, eventResponse.getStatus())
		);
	}

	@Test
	void voerUit_verbodenToegang_geeftJuisteEventResponseTerug(@Mock KamerService kamerService) {
		doThrow(VerbodenToegangException.class).when(kamerService).highlightKaart(
			sut.getDeelnemerId(),
			kaartToelichting
		);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).highlightKaart(sut.getDeelnemerId(), kaartToelichting),
			() -> assertEquals(EventResponse.Status.VERBODEN, eventResponse.getStatus())
		);
	}

	@Test
	void voerUit_geeftJuisteEventResponseTerug(@Mock KamerService kamerService) {
		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).highlightKaart(sut.getDeelnemerId(), kaartToelichting),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertEquals(kaartToelichting, eventResponse.getContext().get("gehighlighteKaart")),
			() -> assertTrue(eventResponse.isStuurNaarAlleClients())
		);
	}

}
