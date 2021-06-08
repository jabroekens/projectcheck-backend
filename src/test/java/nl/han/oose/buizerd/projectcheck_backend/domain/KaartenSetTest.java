package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KaartenSetTest {

	private static final long ID = 1L;

	@Mock
	private Rol rol;

	@Spy
	private Set<Kaart> kaarten = new HashSet<>();

	private KaartenSet sut;

	@BeforeEach
	void setUp() {
		sut = new KaartenSet(ID, rol, kaarten);
	}

	@Test
	void getId_geeftJuisteWaarde() {
		assertEquals(ID, sut.getId());
	}

	@Test
	void getKaarten_geeftJuisteWaarden() {
		assertIterableEquals(kaarten, sut.getKaarten());
	}

	@Test
	void getRol_geeftJuisteWaarde() {
		assertEquals(rol, sut.getRol());
	}

}
