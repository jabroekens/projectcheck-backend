package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KamerTest {

	private static final String KAMER_CODE = "123456";

	@Mock
	private LocalDateTime datum;

	@Mock
	private Begeleider begeleider;

	/*
	* Deze set kan niet worden gemokt, omdat er niks kan worden toegevoegd aan een list wanneer deze gemocked is.
	* Daarom wordt er hier gebruik gemaakt van een Spy die de Set gedeeltelijk mocked.
	*/
	@Spy
	private Set<Deelnemer> deelnemers;

	private Kamer kamer;

	@BeforeEach
	void init() {
		deelnemers = new HashSet<>();
		kamer = new Kamer(KamerTest.KAMER_CODE, datum, begeleider, deelnemers);
	}

	@Test
	void geeftJuisteKamerCode() {
		Assertions.assertEquals(KamerTest.KAMER_CODE, kamer.getKamerCode());
	}

	@Test
	void geeftJuisteDatum() {
		Assertions.assertEquals(datum, kamer.getDatum());
	}

	@Test
	void geeftJuisteBegeleider() {
		Assertions.assertEquals(begeleider, kamer.getBegeleider());
	}

	@Test
	void geeftJuisteDeelnemer(@Mock DeelnemerId deelnemerId) {
		Assertions.assertTrue(kamer.getDeelnemer(deelnemerId).isEmpty());
	}

	@Test
	void geeftJuisteDeelnemers() {
		/*
		 * Omdat `Kamer#getDeelnemers()` een UnmodifiableSet teruggeeft
		 * (wat in werkelijkheid een UnmodifiableCollection is), en die
		 * niet een eigen `equals` methode implementeert, moeten
		 * `expected` en `actual` omgedraaid worden.
		 *
		 * Zie: https://stackoverflow.com/a/31733658
		 */
		Assertions.assertEquals(kamer.getDeelnemers(), deelnemers);
		Assertions.assertAll(
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> kamer.getDeelnemers().add(null)),
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> kamer.getDeelnemers().remove(null))
		);
	}
	@Test
	void kamerGeeftJuisteDeelnemerId(){
		//Arrange
		Kamer testKamer = new Kamer(KamerTest.KAMER_CODE, datum, begeleider, deelnemers);
		Deelnemer deelnemer = Mockito.mock(Deelnemer.class);

		testKamer.voegDeelnemerToe(deelnemer);

		int expectedId = 3;
		//Act
		int actualId = Math.toIntExact(kamer.genereerDeelnemerId());
		//Assert
		Assertions.assertEquals(expectedId,actualId);



	}


}
