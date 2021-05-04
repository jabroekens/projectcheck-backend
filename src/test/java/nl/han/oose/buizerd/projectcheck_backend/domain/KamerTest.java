package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class KamerTest {

	private Kamer kamer;

	@BeforeEach
	void init() {
		kamer = new Kamer();
	}

	/*
	 * XXX Misschien een package-private setter voor een eigen code generator?
	 *  * Dit maakt het ook mogelijk de uitkomst van `KamerRepository#maakKamer(String)`
	 *    voorspelbaar te maken (zie comment bij KamerRepositoryTest)
	 */
	@Test
	void genereertUniekeCode() {
		try (MockedStatic<Kamer> kamer = Mockito.mockStatic(Kamer.class)) {
			kamer.when(Kamer::genereerCode).thenReturn("123456");
			Assertions.assertEquals("123456", Kamer.genereerCode());
		}
	}

	@Test
	void zetJuisteBegeleider() {
		Begeleider begeleider = Mockito.mock(
			Begeleider.class,
			Mockito.withSettings().useConstructor(
				Mockito.mock(
					DeelnemerId.class, Mockito.withSettings().useConstructor(kamer)
				),
				"Joost"
			)
		);

		Assertions.assertNull(kamer.getBegeleider());
		kamer.setBegeleider(begeleider);
		Assertions.assertEquals(begeleider, kamer.getBegeleider());
	}

	@Test
	void geeftJuistAantalDeelnemers() {
		Assertions.assertEquals(0, kamer.getAantalDeelnemers());
		Begeleider begeleider = Mockito.mock(
			Begeleider.class,
			Mockito.withSettings().useConstructor(
				Mockito.mock(
					DeelnemerId.class, Mockito.withSettings().useConstructor(kamer)
				),
				"Joost"
			)
		);

		kamer.setBegeleider(begeleider);
		Assertions.assertEquals(1, kamer.getAantalDeelnemers());
	}

}
