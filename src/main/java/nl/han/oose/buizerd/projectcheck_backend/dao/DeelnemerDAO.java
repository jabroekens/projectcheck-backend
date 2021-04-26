package nl.han.oose.buizerd.projectcheck_backend.dao;

import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;

public class DeelnemerDAO extends DAO<Deelnemer, DeelnemerId> {

	/**
	 * Constructs a {@code DeelnemerDAO}.
	 */
	public DeelnemerDAO() {
		super(Deelnemer.class);
	}

}
