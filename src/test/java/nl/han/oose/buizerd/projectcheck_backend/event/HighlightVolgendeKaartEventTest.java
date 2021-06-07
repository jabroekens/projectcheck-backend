package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class HighlightVolgendeKaartEventTest {

	private HighlightVolgendeKaartEvent highlightVolgendeKaartEvent;
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
		deelnemer = Mockito.mock(Begeleider.class);
		kaart = Mockito.mock(Kaart.class);
		ronde = new Ronde();

	}

	/**
	 * In deze test word eerst de kaart toelichting gezet, om vervolgens te kijken of deze weer null wordt,
	 * door het aanroepen van highlighten volgende kaart.
	 * Op deze manier kan er worden gekeken of dit event werkt.
	 */
	@Test
	void VolgendeKaartKanWordenGehighlight() {
		//Arrange
		highlightVolgendeKaartEvent = new HighlightVolgendeKaartEvent();
		highlightenKaartEvent = new HighlightenKaartEvent();

		Mockito.when(kamer.getHuidigeRonde()).thenReturn(ronde);
		Mockito.when(deelnemer.getKamer()).thenReturn(kamer);

		highlightenKaartEvent.kaartToelichting = Mockito.mock(KaartToelichting.class);

		//Act

		highlightenKaartEvent.voerUit(deelnemer, session);
		highlightVolgendeKaartEvent.voerUit(deelnemer, session);

		//Assert
		Assertions.assertEquals(Optional.empty(), ronde.getGehighlighteKaart());

	}

}
