package nl.han.oose.buizerd.projectcheck_backend.exception;

class DeelnemerRolNietGevondenExceptionTest extends ExceptionTest<DeelnemerRolNietGevondenException> {

	public static final String NAAM = "Piet";

	public DeelnemerRolNietGevondenExceptionTest() {
		super("Deelnemer " + DeelnemerRolNietGevondenExceptionTest.NAAM + " heeft nog geen rol.");
	}

	@Override
	protected DeelnemerRolNietGevondenException setUpImpl() {
		return new DeelnemerRolNietGevondenException(DeelnemerRolNietGevondenExceptionTest.NAAM);
	}

}
