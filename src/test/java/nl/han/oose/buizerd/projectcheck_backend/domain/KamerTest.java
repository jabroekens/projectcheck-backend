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

	@Test
	void genereertUniekeCode() {
		try (MockedStatic<Kamer> mock = Mockito.mockStatic(Kamer.class)) {
			mock.when(Kamer::genereerCode).thenReturn("123456");
			Assertions.assertEquals(Kamer.genereerCode(), "123456");
		}
	}

	@Test
	void zetJuisteBegeleider() {
		Begeleider begeleider = Mockito.mock(
			Begeleider.class,
			Mockito.withSettings().useConstructor(kamer, "Joost")
		);

		Assertions.assertNull(kamer.getBegeleider());
		kamer.setBegeleider(begeleider);
		Assertions.assertEquals(kamer.getBegeleider(), begeleider);
	}

	@Test
	void geeftJuistAantalDeelnemers() {
		Assertions.assertEquals(0, kamer.getAantalDeelnemers());
		Begeleider begeleider = Mockito.mock(
			Begeleider.class,
			Mockito.withSettings().useConstructor(kamer, "Joost")
		);

		kamer.setBegeleider(begeleider);
		Assertions.assertEquals(1, kamer.getAantalDeelnemers());
	}

}
