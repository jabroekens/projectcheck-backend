package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RondeTest {

	private Ronde sut;

	@BeforeEach
	void setUp() {
		sut = new Ronde();
	}

	@Test
	void setEnGetGehighlighteKaart_zetEnGeeftJuisteWaarde(@Mock KaartToelichting kaartToelichting) {
		sut.setGehighlighteKaart(kaartToelichting);

		assertAll(
			() -> assertTrue(sut.getGehighlighteKaart().isPresent()),
			() -> assertEquals(kaartToelichting, sut.getGehighlighteKaart().get())
		);
	}

}
