package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.CodeGenerator;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;

public class AppServiceImpl implements AppService {

	private DAO dao;
	private CodeGenerator codeGenerator;

	public DeelnemerId maakKamer(String begeleiderNaam) {
		var kamerCode = codeGenerator.genereerCode(Kamer.KAMER_CODE_MAX_LENGTE);

		var deelnemerId = new DeelnemerId(1L, kamerCode);
		var begeleider = new Begeleider(deelnemerId, begeleiderNaam);
		var kamer = new Kamer(kamerCode, begeleider);

		dao.create(kamer);
		return deelnemerId;
	}

	@Transactional
	public DeelnemerId neemDeel(String kamerCode, String deelnemerNaam)
	throws KamerNietGevondenException, KamerGeslotenException {
		var kamer = dao.read(Kamer.class, kamerCode);

		if (kamer.isPresent()) {
			if (kamer.get().getKamerFase() != KamerFase.OPEN) {
				throw new KamerGeslotenException(kamerCode);
			}

			var deelnemerId = kamer.get().genereerDeelnemerId();
			var deelnemer = new Deelnemer(
				new DeelnemerId(deelnemerId, kamerCode),
				deelnemerNaam
			);

			kamer.get().addDeelnemer(deelnemer);
			dao.update(kamer.get());
			return deelnemer.getDeelnemerId();
		} else {
			throw new KamerNietGevondenException(kamerCode);
		}
	}

	@Inject
	void setDao(DAO dao) {
		this.dao = dao;
	}

	@Inject
	void setCodeGenerator(CodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}

}
