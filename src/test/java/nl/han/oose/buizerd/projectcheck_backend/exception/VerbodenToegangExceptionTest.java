package nl.han.oose.buizerd.projectcheck_backend.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VerbodenToegangExceptionTest extends ExceptionTest<VerbodenToegangException> {

	private static final Long DEELNEMER_ID = 1L;

	private static final String KAMER_CODE = "123456";

	public VerbodenToegangExceptionTest() {
		super("Deelnemer met id {" + DEELNEMER_ID + "} in kamer met kamercode {" + KAMER_CODE + "} heeft geen toegang");
	}

	@Override
	protected VerbodenToegangException setUpImpl() {
		return new VerbodenToegangException(DEELNEMER_ID, KAMER_CODE);
	}

}
