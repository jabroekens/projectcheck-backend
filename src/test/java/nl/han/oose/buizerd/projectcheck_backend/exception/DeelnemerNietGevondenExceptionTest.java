package nl.han.oose.buizerd.projectcheck_backend.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeelnemerNietGevondenExceptionTest extends ExceptionTest<DeelnemerNietGevondenException> {

	private static final Long DEELNEMER_ID = 1L;

	private static final String KAMER_CODE = "123456";

	public DeelnemerNietGevondenExceptionTest() {
		super("Deelnemer met id {" + DEELNEMER_ID + "} is niet gevonden voor kamer met kamercode {" + KAMER_CODE + "}");
	}

	@Override
	protected DeelnemerNietGevondenException setUpImpl() {
		return new DeelnemerNietGevondenException(DEELNEMER_ID, KAMER_CODE);
	}

}
