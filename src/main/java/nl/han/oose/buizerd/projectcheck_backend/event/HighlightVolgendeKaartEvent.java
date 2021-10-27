package nl.han.oose.buizerd.projectcheck_backend.event;

import nl.han.oose.buizerd.projectcheck_backend.exception.RondeNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

public class HighlightVolgendeKaartEvent extends Event {

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		try {
			kamerService.highlightVolgendeKaart(deelnemerId);
			return new EventResponse(EventResponse.Status.OK)
				.metContext("gehighlighteKaart", null)
				.stuurNaarAlleClients();
		} catch (RondeNietGevondenException e) {
			return new EventResponse(EventResponse.Status.RONDE_NIET_GEVONDEN);
		} catch (VerbodenToegangException e) {
			return new EventResponse(EventResponse.Status.VERBODEN);
		}
	}

}
