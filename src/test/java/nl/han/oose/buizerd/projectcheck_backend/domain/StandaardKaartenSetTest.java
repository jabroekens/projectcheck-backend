package nl.han.oose.buizerd.projectcheck_backend.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StandaardKaartenSetTest {

	@ParameterizedTest
	@EnumSource(StandaardKaartenSet.class)
	void kaartenSetIsNotNull(StandaardKaartenSet standaardKaartenSet) {
		Assertions.assertAll(
			() -> Assertions.assertNotNull(standaardKaartenSet.getKaartenSet()),
			() -> Assertions.assertNotNull(standaardKaartenSet.getKaartenSet().getKaarten()),
			() -> Assertions.assertNotNull(standaardKaartenSet.getKaartenSet().getRol()),
			() -> Assertions.assertNotNull(standaardKaartenSet.getKaartenSet().getId())
		);
	}

}
