package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KaartenSetTest {

	private static final long ID = 1L;

	@Mock
	private Rol rol;

	@Spy
	private Set<Kaart> kaarten;

	private KaartenSet kaartenSet;

	@BeforeEach
	void setup() {
		kaarten = new HashSet<>();
		kaartenSet = new KaartenSet(ID, rol, kaarten);
	}

	@Test
	void geeftJuisteIdTerug() {
		Assertions.assertEquals(ID, kaartenSet.getId());
	}

	@Test
	void geeftJuisteKaartenTerug() {
		Assertions.assertIterableEquals(kaarten, kaartenSet.getKaarten());
	}

	@Test
	void geeftJuisteRolTerug() {
		Assertions.assertEquals(rol, kaartenSet.getRol());
	}

}
