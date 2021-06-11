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
		var kaartenSelectie = deelnemer.getKaartenSelectie();
		if (kaartenSelectie == null) {
			kaartenSelectie = new KaartenSelectie();
			deelnemer.setKaartenSelectie(kaartenSelectie);
		}

		if (kaartenSelectie.kaartIsGeselecteerd(geselecteerdeKaart)) {
			kaartenSelectie.removeKaart(geselecteerdeKaart);
			return new EventResponse(EventResponse.Status.OK).metContext("verwijderdeKaart", geselecteerdeKaart);
		} else if (!kaartenSelectie.isVol()) {
			kaartenSelectie.addKaart(geselecteerdeKaart);
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
