package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.exception.KaartenSelectieVolException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KaartNaarSelectieEventTest {

	@Mock
	private Kaart geselecteerdeKaart;

	@Mock
	private KamerService kamerService;

	private KaartNaarSelectieEvent sut;

	@BeforeEach
	void setUp() {
		sut = new KaartNaarSelectieEvent();
		sut.geselecteerdeKaart = geselecteerdeKaart;
	}

	@Test
	void voerUit_geselecteerdeKaartToegevoegd_geeftJuisteEventResponseTerug() {
		when(kamerService.kaartNaarSelectie(sut.getDeelnemerId(), geselecteerdeKaart)).thenReturn(true);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).kaartNaarSelectie(sut.getDeelnemerId(), geselecteerdeKaart),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertEquals(geselecteerdeKaart, eventResponse.getContext().get("toegevoegdeKaart"))
		);
	}

	@Test
	void voerUit_geselecteerdeKaartVerwijdert_geeftJuisteEventResponseTerug() {
		when(kamerService.kaartNaarSelectie(sut.getDeelnemerId(), geselecteerdeKaart)).thenReturn(false);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).kaartNaarSelectie(sut.getDeelnemerId(), geselecteerdeKaart),
			() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus()),
			() -> assertEquals(geselecteerdeKaart, eventResponse.getContext().get("verwijderdeKaart"))
		);
	}

	@Test
	void voerUit_kaartenSelectieIsVol_geeftJuisteEventResponseTerug() {
		when(kamerService.kaartNaarSelectie(sut.getDeelnemerId(), geselecteerdeKaart))
			.thenThrow(KaartenSelectieVolException.class);

		var eventResponse = sut.voerUit(kamerService);

		assertAll(
			() -> verify(kamerService).kaartNaarSelectie(sut.getDeelnemerId(), geselecteerdeKaart),
			() -> assertEquals(EventResponse.Status.SELECTIE_VOL, eventResponse.getStatus()),
			() -> assertEquals(geselecteerdeKaart, eventResponse.getContext().get("ongebruikteKaart"))
		);
	}

}
