package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KaartenSelectieTest {

	@Spy
	private Set<Kaart> kaarten = new HashSet<>();

	private KaartenSelectie kaartenSelectie;

	@BeforeEach
	void setUp() {
		kaartenSelectie = new KaartenSelectie(kaarten);
	}

	@Test
	void kaartIsGeselecteerd_geeftTrueAlsDezeGeselecteerdIs(@Mock Kaart kaart) {
		kaartenSelectie.addKaart(kaart);
		assertTrue(kaartenSelectie.kaartIsGeselecteerd(kaart));
	}

	@Test
	void addKaart_voegtKaartNietToeAlsDeSelectieVolIs(@Mock Kaart kaart) {
		Mockito.doReturn(KaartenSelectie.MAX_KAARTEN).when(kaarten).size();

		kaartenSelectie.addKaart(kaart);

		Mockito.verify(kaarten).size();
		Mockito.verifyNoMoreInteractions(kaarten);
	}

	@Test
	void removeKaart_verwijdertKaart(@Mock Kaart kaart) {
		kaarten.add(kaart);

		kaartenSelectie.removeKaart(kaart);

		assertFalse(kaarten.contains(kaart));
	}

	@Test
	void isVol_geeftTrueAlsKaartenSelectieVolIs() {
		Mockito.doReturn(KaartenSelectie.MAX_KAARTEN).when(kaarten).size();
		assertTrue(kaartenSelectie.isVol());
	}

}
