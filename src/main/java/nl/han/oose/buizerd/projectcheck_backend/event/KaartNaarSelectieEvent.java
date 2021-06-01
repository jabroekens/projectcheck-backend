package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSelectie;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

public class KaartNaarSelectieEvent extends Event {

	@NotNull
	@Valid
	Kaart geselecteerdeKaart;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		KaartenSelectie ks = deelnemer.getKaartenSelectie();
		if (ks == null) {
			ks = new KaartenSelectie();
			deelnemer.setKaartenSelectie(ks);
		}

		if (ks.kaartIsGeselecteerd(geselecteerdeKaart)) {
			ks.removeKaart(geselecteerdeKaart);
			return new EventResponse(EventResponse.Status.OK).metContext("verwijderdeKaart", geselecteerdeKaart);
		} else if (!ks.isVol()) {
			ks.addKaart(geselecteerdeKaart);
			return new EventResponse(EventResponse.Status.OK).metContext("toegevoegdeKaart", geselecteerdeKaart);
		} else {
			return new EventResponse(EventResponse.Status.SELECTIE_VOL).metContext("ongebruikteKaart", geselecteerdeKaart);
		}
	}

	@Override
	protected void handelAf(DAO dao, Kamer kamer) {
		dao.update(kamer);
	}

}
