package nl.han.oose.buizerd.projectcheck_backend.exception;

class KamerGeslotenExceptionTest extends ExceptionTest<KamerGeslotenException> {

	private static final String KAMER_CODE = "123456";

	public KamerGeslotenExceptionTest() {
		super("Kamer met kamercode {" + KAMER_CODE + "} is gesloten");
	}

	@Override
	protected KamerGeslotenException setUpImpl() {
		return new KamerGeslotenException(KAMER_CODE);
	}

}
