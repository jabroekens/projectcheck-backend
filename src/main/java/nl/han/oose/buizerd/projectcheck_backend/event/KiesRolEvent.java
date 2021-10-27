package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;

/**
 * Zet de rol van de {@link Deelnemer}.
 */
public class KiesRolEvent extends Event {

	@NotNull
	@Valid
	Rol rol;

	@Override
	public EventResponse voerUit(KamerService kamerService) {
		kamerService.kiesRol(super.getDeelnemerId(), rol);
		return new EventResponse(EventResponse.Status.OK).metContext("gekozenRol", rol);
	}

}
