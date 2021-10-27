package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.han.oose.buizerd.projectcheck_backend.dto.DeelnemerKaartenSets;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerHeeftGeenRolException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HaalKaartenSetOpEventTest {

	private HaalKaartenSetOpEvent sut;

	@BeforeEach
	void setUp() {
		sut = new HaalKaartenSetOpEvent();
	}

	@Test
	void voerUit_deelnemerHeeftRol_geeftJuisteEventResponseTerug(
		@Mock KamerService kamerService,
		@Mock DeelnemerKaartenSets deelnemerKaartenSets
	) {
		when(kamerService.getKaartenSets(sut.getDeelnemerId())).thenReturn(deelnemerKaartenSets);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).getKaartenSets(sut.getDeelnemerId()),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertEquals(deelnemerKaartenSets.isGekozen(), eventResponse.getContext().get("gekozen")),
			() -> assertTrue(eventResponse.getContext().get("kaartensets") instanceof Iterable<?>),
			() -> assertIterableEquals(
				deelnemerKaartenSets.getKaartenSets(),
				(Iterable<?>) eventResponse.getContext().get("kaartensets")
			)
		);
	}

	@Test
	void voerUit_deelnemerHeeftGeenRol_geeftJuisteEventResponseTerug(@Mock KamerService kamerService) {
		when(kamerService.getKaartenSets(sut.getDeelnemerId())).thenThrow(DeelnemerHeeftGeenRolException.class);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).getKaartenSets(sut.getDeelnemerId()),
			() -> assertEquals(EventResponse.Status.ROL_NIET_GEVONDEN, eventResponse.getStatus())
		);
	}

}
