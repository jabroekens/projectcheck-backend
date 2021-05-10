package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.BeforeEach;
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

}
