package nl.han.oose.buizerd.projectcheck_backend.domain;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.NoSuchAlgorithmException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CodeGeneratorImplTest {

	private CodeGeneratorImpl sut;

	@BeforeEach
	void setUp() throws NoSuchAlgorithmException {
		sut = new CodeGeneratorImpl();
	}

	@Test
	void genereerCode_isAlphaNumeriek() {
		var expectedPattern = "^[a-zA-Z0-9]{1," + Kamer.KAMER_CODE_MAX_LENGTE + "}$";
		var code = sut.genereerCode(6);
		assertTrue(code.matches(expectedPattern));
	}

}
