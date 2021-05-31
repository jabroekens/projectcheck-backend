package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import java.util.HashSet;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;

public class HaalKaartenSetOpEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer, Session session) {
		// Maak een resultaat aan.
		Set<KaartenSet> kaartenSets = new HashSet<>();
		// Sla op of de deelnemer nog kaartenset(s) moet kiezen.
		boolean gekozen = false;

		// Kijk of de deelnemers rollen 'PROJECTBUREAU' bevat.
		if (deelnemer.getRollen().contains(StandaardRol.PROJECTBUREAU.getRol())) {
			// Itereer over iedere relevante rol binnen een kamer.
			for (Rol kamerRol : deelnemer.getKamer().getRelevanteRollen()) {
				// Voeg iedere kaartenset toe aan het resultaat.
				kaartenSets.addAll(kamerRol.getKaartenSets());
			}
		} else {
			// Loop over de rollen van de deelnemer.
			for (Rol rol : deelnemer.getRollen()) {
				// Voeg iedere kaartenset toe aan het resultaat.
				kaartenSets = rol.getKaartenSets();
			}
			gekozen = true;
		}

		// Geef de kaartensets terug aan de gebruiker.
		return new EventResponse(EventResponse.Status.OK)
			.antwoordOp(this)
			.metContext("gekozen", gekozen)
			.metContext("kaartensets", kaartenSets);
	}

}
