package nl.han.oose.buizerd.projectcheck_backend.exception;

public class OngeldigeNaamExceptionTest extends ExceptionTest<OngeldigeNaamException> {

	private static final String NAAM = "!";

	public OngeldigeNaamExceptionTest() {
		super("Ongeldige naam: " + OngeldigeNaamExceptionTest.NAAM);
	}

	@Override
	protected OngeldigeNaamException setUpImpl() {
		return new OngeldigeNaamException(OngeldigeNaamExceptionTest.NAAM);
	}

}
