package nl.han.oose.buizerd.projectcheck_backend.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RondeNietGevondenExceptionTest extends ExceptionTest<RondeNietGevondenException> {

	private static final String KAMER_CODE = "123456";

	public RondeNietGevondenExceptionTest() {
		super("Geen ronde gevonden voor kamer met kamercode {" + KAMER_CODE + "}");
	}

	@Override
	protected RondeNietGevondenException setUpImpl() {
		return new RondeNietGevondenException(KAMER_CODE);
	}

}
