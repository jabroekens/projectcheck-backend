package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;

public class KiesRelevanteRollenEvent extends Event {

	@NotNull
	@Valid
	private String rolNaam;

	@NotNull
	@Valid
	private DeelnemerId deelnemerId;

	@Override
	protected EventResponse voerUit(Kamer kamer, Session session) {
		Optional<Rol> rol = kamer.getRelevanteRol(rolNaam);
		Begeleider begeleider = kamer.getBegeleider();
		if (rol.isPresent() && begeleider.getDeelnemerId() == deelnemerId) {
			kamer.activeerRelevanteRol(rol.get());
			String bericht = String.format("de rol %s is ingeschakeld voor de kamer %s", rol.get().getRolNaam(), kamer.getKamerCode());
			return new EventResponse(EventResponse.Status.OK).metContext("bericht", bericht);
		} else {
			return new EventResponse(EventResponse.Status.ROL_NIET_GEVONDEN).metContext("rolNaam", rolNaam);
		}
	}

}
