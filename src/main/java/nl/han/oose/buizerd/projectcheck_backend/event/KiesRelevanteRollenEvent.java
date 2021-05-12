package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.validation.constraints.NotNull;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

public class KiesRelevanteRollenEvent extends Event {

	@NotNull
	private String rolNaam;

	public String getRolNaam() {
		return rolNaam;
	}

	@Override
	protected void voerUit(Kamer kamer, Session session) throws IOException {
		Optional<Rol> rol = kamer.getRelevanteRol(getRolNaam());
		if (rol.isPresent()) {
			session.getBasicRemote().sendText(rol.get().getRolNaam() + " is ingeschakeld voor kamer " + kamer);
		}
	}

	@Override
	protected void handelAf(KamerRepository kamerRepository, Kamer kamer) throws IOException {
		// Doe niets
	}

}
