package nl.han.oose.buizerd.projectcheck_backend.service;

import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;

@Path("/")
public class AppService {

	@Context
	UriInfo uriInfo;

	@Inject
	private KamerRepository kamerRepository;

	private final Map<String, KamerService> kamerServices;

	public AppService() {
		this.kamerServices = new HashMap<>();
	}

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam De naam van de {@link Begeleider}.
	 * @return Een JSON string met de WebSocket URL van de {@link KamerService} gewikkelt in {@link Response}.
	 * @see KamerRepository#maakKamer(String)
	 */
	@Path("/kamer/nieuw")
	@POST
	@Produces("application/json")
	public Response maakKamer(@FormParam("begeleiderNaam") String begeleiderNaam) {
		Kamer kamer = kamerRepository.maakKamer(begeleiderNaam);
		KamerService kamerService = kamerServices.put(kamer.getKamerCode(), new KamerService(kamer));

		String url = "wss://" + uriInfo.getBaseUri().getHost() + "/kamer/" + kamer.getKamerCode();
		JsonObject json = Json.createObjectBuilder().add("kamer_url", url).build();
		return Response.ok(json).build();
	}

}
