package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.exception.KaartenSelectieVolException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

public class KaartNaarSelectieEvent extends Event {

	@NotNull
	@Valid
	Kaart geselecteerdeKaart;

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		try {
			if (kamerService.kaartNaarSelectie(super.getDeelnemerId(), geselecteerdeKaart)) {
				return new EventResponse(EventResponse.Status.OK).metContext("toegevoegdeKaart", geselecteerdeKaart);
			}

			return new EventResponse(EventResponse.Status.OK).metContext("verwijderdeKaart", geselecteerdeKaart);
		} catch (KaartenSelectieVolException e) {
			return new EventResponse(EventResponse.Status.SELECTIE_VOL).metContext(
				"ongebruikteKaart",
				geselecteerdeKaart
			);
		}
	}

}
