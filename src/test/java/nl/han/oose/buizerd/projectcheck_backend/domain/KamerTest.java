package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class KamerTest {

	private static final String KAMER_CODE = "123456";
	private static final LocalDateTime DATUM = LocalDateTime.now();
	private static final Set<Deelnemer> DEELNEMERS = new HashSet<>();

	private Kamer kamer;

	@BeforeEach
	void init() {
		kamer = new Kamer(KamerTest.KAMER_CODE, KamerTest.DATUM, KamerTest.DEELNEMERS);
	}

	@Test
	void geeftJuisteKamerCode() {
		Assertions.assertEquals(KamerTest.KAMER_CODE, kamer.getKamerCode());
	}

	@Test
	void geeftJuisteDatum() {
		Assertions.assertEquals(KamerTest.DATUM, kamer.getDatum());
	}

	@Test
	void zetEnGeeftJuisteBegeleider() {
		Begeleider begeleider = Mockito.mock(
			Begeleider.class,
			Mockito.withSettings().useConstructor(
				Mockito.mock(DeelnemerId.class, Mockito.withSettings().useConstructor(1L, kamer.getKamerCode())),
				"Joost"
			)
		);

		Assertions.assertNull(kamer.getBegeleider());
		kamer.setBegeleider(begeleider);
		Assertions.assertEquals(begeleider, kamer.getBegeleider());
	}

	@Test
	void geeftJuisteDeelnemers() {
		Assertions.assertEquals(KamerTest.DEELNEMERS, kamer.getDeelnemers());
		Assertions.assertAll(
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> kamer.getDeelnemers().add(null)),
			() -> Assertions.assertThrows(UnsupportedOperationException.class, () -> kamer.getDeelnemers().remove(null))
		);
	}

}
