package nl.han.oose.buizerd.projectcheck_backend.exception;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KaartenSelectieVolExceptionTest extends ExceptionTest<KaartenSelectieVolException> {

	private static final Long DEELNEMER_ID = 1L;

	private static final String KAMER_CODE = "123456";

	public KaartenSelectieVolExceptionTest() {
		super("De kaartenselectie voor deelnemer met id {" + DEELNEMER_ID + "} in kamer met kamercode {" + KAMER_CODE +
		      "} is vol");
	}

	@Override
	protected KaartenSelectieVolException setUpImpl() {
		return new KaartenSelectieVolException(DEELNEMER_ID, KAMER_CODE);
	}

}
