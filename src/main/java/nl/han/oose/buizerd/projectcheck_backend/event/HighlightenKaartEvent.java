package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.exception.RondeNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

public class HighlightenKaartEvent extends Event {

	@NotNull
	@Valid
	KaartToelichting kaartToelichting;

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		try {
			kamerService.highlightKaart(super.getDeelnemerId(), kaartToelichting);
			return new EventResponse(EventResponse.Status.OK)
				.metContext("gehighlighteKaart", kaartToelichting)
				.stuurNaarAlleClients();
		} catch (VerbodenToegangException e) {
			return new EventResponse(EventResponse.Status.VERBODEN);
		} catch (RondeNietGevondenException e) {
			return new EventResponse(EventResponse.Status.RONDE_NIET_GEVONDEN);
		}
	}

}
