package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSelectie;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;

public class KaartNaarSelectieEvent extends Event {

	Kaart geselecteerdeKaart;

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		KaartenSelectie ks = deelnemer.getSelectie();
		if (ks == null) {
			ks = new KaartenSelectie();
			deelnemer.setSelectie(ks);
		}
		if (ks.kaartExists(geselecteerdeKaart)) {
			ks.removeKaart(geselecteerdeKaart);
			return new EventResponse(EventResponse.Status.OK).metContext("verwijderdeKaart", geselecteerdeKaart);
		} else if (ks.getLength() <= 3) {
			ks.addKaart(geselecteerdeKaart);
			return new EventResponse(EventResponse.Status.OK).metContext("toegevoegdeKaart", geselecteerdeKaart);
		} else {
			return new EventResponse(EventResponse.Status.SELECTIE_VOL).metContext("ongebruikteKaart", geselecteerdeKaart);
		}
	}

	@Override
	protected void handelAf(DAO<Kamer, String> kamerDAO, Kamer kamer) {
		kamerDAO.update(kamer);
	}

}
