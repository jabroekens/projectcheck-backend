package nl.han.oose.buizerd.projectcheck_backend.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeelnemerHeeftGeenRolExceptionTest extends ExceptionTest<DeelnemerHeeftGeenRolException> {

	private static final Long DEELNEMER_ID = 1L;

	private static final String KAMER_CODE = "123456";

	public DeelnemerHeeftGeenRolExceptionTest() {
		super("Deelnemer met deelnemer id {" + DEELNEMER_ID + "} in kamer met kamercode {" + KAMER_CODE +
		      "} heeft geen rol");
	}

	@Override
	protected DeelnemerHeeftGeenRolException setUpImpl() {
		return new DeelnemerHeeftGeenRolException(DEELNEMER_ID, KAMER_CODE);
	}

}
