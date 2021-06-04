package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import java.util.HashSet;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerRolNietGevondenException;

public class HaalKaartenSetOpEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		// Kijk of de deelnemer een rol heeft.
		if (deelnemer.getRol() == null) {
			throw new DeelnemerRolNietGevondenException(deelnemer.getNaam());
		}

		// Maak een resultaat aan.
		Set<KaartenSet> kaartenSets = new HashSet<>();
		// Sla op of de deelnemer nog kaartenset(s) moet kiezen.
		boolean gekozen = false;

		// Kijk of de deelnemers rollen 'PROJECTBUREAU' bevat.
		if (deelnemer.getRol().equals(StandaardRol.PROJECTBUREAU.getRol())) {
			// Itereer over iedere relevante rol binnen een kamer.
			for (Rol kamerRol : deelnemer.getKamer().getRelevanteRollen()) {
				// Voeg iedere kaartenset toe aan het resultaat.
				kaartenSets.addAll(kamerRol.getKaartenSets());
			}
		} else {
			// Voeg de kaartensets toe aan het resultaat.
			kaartenSets.addAll(deelnemer.getRol().getKaartenSets());
			gekozen = true;
		}

		// Geef de kaartensets terug aan de gebruiker.
		return new EventResponse(EventResponse.Status.OK)
			.antwoordOp(this)
			.metContext("gekozen", gekozen)
			.metContext("kaartensets", kaartenSets);
	}

}
