package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.JsonObject;
import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

@Path("/")
public class AppService extends Application {

	@Inject
	private KamerRepository kamerRepository;

	@Inject
	private KamerService kamerService;

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam De naam van de {@link Begeleider}.
	 * @return Een JSON string met de WebSocket URL van de {@link Kamer} gewikkelt in {@link Response}.
	 * @see KamerRepository#maakKamer(String)
	 */
	@Path("/kamer/nieuw")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response maakKamer(@FormParam("begeleiderNaam") @NotNull String begeleiderNaam) {
		// FIXME Foutafhandeling als begeleiderNaam leeg of null is
		Kamer kamer = kamerRepository.maakKamer(begeleiderNaam);
		KamerService.registreer(kamer.getKamerCode());

		JsonObject json = new JsonObject();
		json.addProperty("kamer_url", kamerService.getUrl(kamer.getKamerCode()));
		return Response.ok(json.toString()).build();
	}

}
