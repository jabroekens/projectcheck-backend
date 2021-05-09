package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

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
	public Response maakKamer(@FormParam("begeleiderNaam") @Naam String begeleiderNaam) {
		Kamer kamer = kamerRepository.maakKamer(begeleiderNaam);
		KamerService.registreer(kamer.getKamerCode());

		JsonObject json = new JsonObject();
		json.addProperty("kamer_url", kamerService.getUrl(kamer.getKamerCode()));
		return Response.ok(json.toString()).build();
	}

}
