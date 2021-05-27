package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.*;

import java.util.Optional;

public class KaartNaarSelectieEvent extends Event{
    Kaart geselecteerdeKaart;

    @Override
    protected EventResponse voerUit(Kamer kamer, Session session) {

        Optional<Deelnemer> d = kamer.getDeelnemer(deelnemerId);
        //if (d.isPresent()) {
            KaartenSelectie ks = new KaartenSelectie();//d.get().getSelectie();;

            if (ks.kaartExists(geselecteerdeKaart)) {
                ks.removeKaart(geselecteerdeKaart);
                return new EventResponse(EventResponse.Status.OK).metContext("verwijderdeKaart", geselecteerdeKaart);
            } else if (ks.getLength() < 3) {
                ks.addKaart(geselecteerdeKaart);
                return new EventResponse(EventResponse.Status.OK).metContext("toegevoegdeKaart", geselecteerdeKaart);
            } else {
                return new EventResponse(EventResponse.Status.SELECTIE_VOL).metContext("ongebruikteKaart", geselecteerdeKaart);
            }
       // } else {
       //     return new EventResponse(EventResponse.Status.INVALIDE).metContext("deelnemer niet gevonden", null);
        //}
    }
}
