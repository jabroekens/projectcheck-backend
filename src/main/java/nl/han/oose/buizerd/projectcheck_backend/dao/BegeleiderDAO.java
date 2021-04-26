package nl.han.oose.buizerd.projectcheck_backend.dao;

import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;

public class BegeleiderDAO extends DAO<Begeleider, DeelnemerId> {

	/**
	 * Constructs a {@code BegeleiderDAO}.
	 */
	public BegeleiderDAO() {
		super(Begeleider.class);
	}

}
