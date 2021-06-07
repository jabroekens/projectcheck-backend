package nl.han.oose.buizerd.projectcheck_backend.exception;

class KamerNietGevondenExceptionTest extends ExceptionTest<KamerNietGevondenException> {

	private static final String KAMER_CODE = "123456";

	public KamerNietGevondenExceptionTest() {
		super("Kamer met kamercode {" + KAMER_CODE + "} is niet gevonden");
	}

	@Override
	protected KamerNietGevondenException setUpImpl() {
		return new KamerNietGevondenException(KAMER_CODE);
	}

}
