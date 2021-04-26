package nl.han.oose.buizerd.projectcheck_backend.endpoint;

import java.util.HashMap;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import nl.han.oose.buizerd.projectcheck_backend.dao.BegeleiderDAO;
import nl.han.oose.buizerd.projectcheck_backend.dao.SpelDAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Spel;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.repository.SpelRepository;

@Path("/")
public class AppService {

	// https://stackoverflow.com/a/7673188
	@Context
	UriInfo uriInfo;

	private final SpelRepository spelRepository;
	private final Map<Long, SpelService> spelServices;

	public AppService() {
		this.spelRepository = new SpelRepository(new SpelDAO(), new BegeleiderDAO());
		this.spelServices = new HashMap<>();
	}

	/**
	 * Makes a room available and returns the WebSocket URI through which can be communicated with the room.
	 *
	 * @param begeleiderNaam The name of the {@link Begeleider} for the room.
	 */
	@Path("/spel/nieuw")
	@POST
	@Produces("application/json")
	public Response maakSpel(@FormParam("begeleiderNaam") String begeleiderNaam) {
		Spel spel = spelRepository.maakSpel(begeleiderNaam);
		SpelService spelService = spelServices.put(spel.getSpelId(), new SpelService(spel));

		String uri = "wss://" + uriInfo.getBaseUri() + "/spel/" + spel.getSpelId();
		JsonObject json = Json.createObjectBuilder().add("room_websocket_url", uri).build();
		return Response.ok(json).build();
	}

}
