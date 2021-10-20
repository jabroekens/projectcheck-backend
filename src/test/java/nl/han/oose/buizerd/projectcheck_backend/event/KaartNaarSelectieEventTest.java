package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSelectie;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KaartNaarSelectieEventTest {

	@Mock
	private Kaart geselecteerdeKaart;

	private KaartNaarSelectieEvent kaartNaarSelectieEvent;

	@BeforeEach
	void setUp() {
		kaartNaarSelectieEvent = new KaartNaarSelectieEvent();
		kaartNaarSelectieEvent.geselecteerdeKaart = geselecteerdeKaart;
	}

	@Test
	void handelAf_updateKamer(@Mock DAO dao, @Mock Kamer kamer) {
		kaartNaarSelectieEvent.handelAf(dao, kamer);
		verify(dao).update(kamer);
	}

	@Nested
	class voerUit {

		@Mock
		private Deelnemer deelnemer;

		@Mock
		private KaartenSelectie kaartenSelectie;

		@BeforeEach
		void setUp() {
			Mockito.when(deelnemer.getKaartenSelectie()).thenReturn(kaartenSelectie);
		}

		@Test
		void kaartenSelectieBestaatNiet_maaktKaartenSelectie() {
			Mockito.when(deelnemer.getKaartenSelectie()).thenReturn(null);
			kaartNaarSelectieEvent.voerUit(deelnemer);
			verify(deelnemer).setKaartenSelectie(Mockito.any());
		}

		@Test
		void kaartIsGeselecteerd_verwijdertKaartEnGeeftJuisteEventResponse() {
			Mockito.when(deelnemer.getKaartenSelectie()).thenReturn(kaartenSelectie);
			Mockito.when(kaartenSelectie.kaartIsGeselecteerd(geselecteerdeKaart)).thenReturn(true);

			EventResponse eventResponse = kaartNaarSelectieEvent.voerUit(deelnemer);

			assertAll(
				() -> verify(kaartenSelectie).removeKaart(geselecteerdeKaart),
				() -> assertEquals(geselecteerdeKaart, eventResponse.getContext().get("verwijderdeKaart")),
				() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
			);
		}

		@Test
		void kaartenSelectieIsNietVol_voegtKaartToeEnGeeftJuisteEventResponse() {
			Mockito.when(deelnemer.getKaartenSelectie()).thenReturn(kaartenSelectie);
			Mockito.when(kaartenSelectie.kaartIsGeselecteerd(geselecteerdeKaart)).thenReturn(false);

			var eventResponse = kaartNaarSelectieEvent.voerUit(deelnemer);

			assertAll(
				() -> verify(kaartenSelectie).addKaart(geselecteerdeKaart),
				() -> assertEquals(geselecteerdeKaart, eventResponse.getContext().get("toegevoegdeKaart")),
				() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
			);
		}

		@Test
		void kaartenSelectieIsVol_geeftJuisteEventResponse() {
			Mockito.when(deelnemer.getKaartenSelectie()).thenReturn(kaartenSelectie);
			Mockito.when(kaartenSelectie.kaartIsGeselecteerd(geselecteerdeKaart)).thenReturn(false);
			Mockito.when(kaartenSelectie.isVol()).thenReturn(true);

			var eventResponse = kaartNaarSelectieEvent.voerUit(deelnemer);

			assertAll(
				() -> assertEquals(geselecteerdeKaart, eventResponse.getContext().get("ongebruikteKaart")),
				() -> assertEquals(EventResponse.Status.SELECTIE_VOL, eventResponse.getStatus())
			);
		}

	}

}
