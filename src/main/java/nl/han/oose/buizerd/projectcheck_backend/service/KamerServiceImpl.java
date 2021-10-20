package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.inject.Inject;
import java.util.concurrent.CompletableFuture;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;

public class KamerServiceImpl implements KamerService {

	private DAO dao;

	public boolean kanDeelnemen(String kamerCode) {
		var kamer = dao.read(Kamer.class, kamerCode);
		return kamer.isPresent() && kamer.get().getKamerFase() != KamerFase.GESLOTEN;
	}

	public CompletableFuture<EventResponse> voerEventUit(Event event)
	throws DeelnemerNietGevondenException, KamerNietGevondenException {
		var kamer = dao.read(Kamer.class, event.getDeelnemerId().getKamerCode());
		if (kamer.isPresent()) {
			var deelnemer = kamer.get().getDeelnemer(event.getDeelnemerId());

			if (deelnemer.isPresent()) {
				return event.voerUit(dao, deelnemer.get());
			} else {
				throw new DeelnemerNietGevondenException(event.getDeelnemerId());
			}
		} else {
			throw new KamerNietGevondenException(event.getDeelnemerId().getKamerCode());
		}
	}

	@Inject
	public void setDao(DAO dao) {
		this.dao = dao;
	}

}
