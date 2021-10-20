package nl.han.oose.buizerd.projectcheck_backend.event;

import java.util.Arrays;
import java.util.stream.Collectors;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;

public class GetStandaardRollenEvent extends Event {

	@Override
	protected EventResponse voerUit(Deelnemer deelnemer) {
		var rollen = Arrays.stream(StandaardRol.values()).map(StandaardRol::getRol).collect(Collectors.toSet());
		return new EventResponse(EventResponse.Status.OK).metContext("standaardRollen", rollen);
	}

}
