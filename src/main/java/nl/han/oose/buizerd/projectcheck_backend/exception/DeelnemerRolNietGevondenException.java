package nl.han.oose.buizerd.projectcheck_backend.exception;

public class DeelnemerRolNietGevondenException extends IllegalStateException {

	public DeelnemerRolNietGevondenException(String naam) {
		super("Deelnemer " + naam + " heeft nog geen rol.");
	}

}
