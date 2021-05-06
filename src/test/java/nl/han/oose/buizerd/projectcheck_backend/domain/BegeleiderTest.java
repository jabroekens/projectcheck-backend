package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BegeleiderTest {

	private DeelnemerId deelnemerId;
	private Begeleider begeleider;

	@BeforeEach
	void init() {
		deelnemerId = Mockito.mock(
			DeelnemerId.class,
			Mockito.withSettings().useConstructor(1L, "123456")
		);

		begeleider = new Begeleider(deelnemerId, "Joost");
	}

	@Test
	void geeftJuisteDeelnemerId() {
		Assertions.assertEquals(deelnemerId, begeleider.getDeelnemerId());
	}

	@Test
	void zetJuisteNaam() {
		begeleider.setNaam("Jochem");
		Assertions.assertEquals("Jochem", begeleider.getNaam());
	}

}
