package nl.han.oose.buizerd.projectcheck_backend.event;

import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

public class GetStandaardRollenEvent extends Event {

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		var rollen = kamerService.getStandaardRollen();
		return new EventResponse(EventResponse.Status.OK).metContext("standaardRollen", rollen);
	}

}
