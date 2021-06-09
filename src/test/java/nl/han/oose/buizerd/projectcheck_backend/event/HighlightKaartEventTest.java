package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HighlightKaartEventTest {

	private HighlightenKaartEvent highlightenKaartEvent;
	private Session session;
	private Kamer kamer;
	private Deelnemer deelnemer;
	private KaartToelichting kaartToelichting;
	private Kaart kaart;
	private Ronde ronde;

	@BeforeEach
	void setup() {
		session = Mockito.mock(Session.class);
		kamer = Mockito.mock(Kamer.class);
		deelnemer = Mockito.mock(Deelnemer.class);
		kaart = Mockito.mock(Kaart.class);
		ronde = new Ronde();

	}

	/**
	 * In deze test word highlighten kaart uitgevoert met een toelichting erbij.
	 * Er wordt vervolgend gekeken of in het antwoord de zelfde toelichting zit en dat in de ronde deze kaart met toelichting zit.
	 * Zo kan er worden gekeken of de kaart wordt gehighlight en of de toelichting correct wordt gezet.
	 */

	@Test
	void kaartKanWordenGehighlightTest() {
		//Arrange
		String expectedToelichting = "Testen";
		kaartToelichting = new KaartToelichting(kaart, expectedToelichting);
		highlightenKaartEvent = new HighlightenKaartEvent();
		highlightenKaartEvent.kaartToelichting = kaartToelichting;
		Mockito.when(kamer.getHuidigeRonde()).thenReturn(java.util.Optional.ofNullable(ronde));
		Mockito.when(deelnemer.getKamer()).thenReturn(kamer);
		//Act
		EventResponse antwoord = highlightenKaartEvent.voerUit(deelnemer, session);
		KaartToelichting kaartUitAntwoord = (KaartToelichting) antwoord.getContext().get("gehighlighteKaart");
		String actualToelichting = kaartUitAntwoord.getToelichting();
		//Assert
		Assertions.assertEquals(expectedToelichting, actualToelichting);
		Assertions.assertEquals(expectedToelichting, ronde.getGehighlighteKaart().get().getToelichting());

	}

	@Test
	void testRondeNietGevonden() {
		//Arrange
		highlightenKaartEvent = new HighlightenKaartEvent();
		Mockito.when(deelnemer.getKamer()).thenReturn(kamer);

		//Act
		EventResponse response = highlightenKaartEvent.voerUit(deelnemer, session);

		//Assert
		Assertions.assertEquals(response.getStatus(), EventResponse.Status.RONDE_NIET_GEVONDEN);

	}

}
