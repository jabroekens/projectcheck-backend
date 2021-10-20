package nl.han.oose.buizerd.projectcheck_backend.controller;

import com.google.gson.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.service.AppService;

@Path("/")
public class AppController {

	private AppService appService;

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @return een {@link Response OK Response} met daarin de kamercode van de aangemaakte kamer
	 *         en het ID van de begeleider in JSON
	 * @see AppService#maakKamer(String)
	 */
	@Path("kamer/nieuw")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response maakKamer(@FormParam("begeleiderNaam") String begeleiderNaam) {
		return Response.ok(getKamerInfo(appService.maakKamer(begeleiderNaam))).build();
	}

	/**
	 * Voegt een deelnemer met de naam {@code deelnemerNaam} toe aan de kamer met kamercode {@code kamerCode}
	 * als de kamer bestaat en open is.
	 *
	 * @see AppService#neemDeel(String, String)
	 */
	@Path("kamer/neemdeel/{kamerCode}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response neemDeel(@PathParam("kamerCode") String kamerCode, @FormParam("deelnemerNaam") String deelnemerNaam) {
		return Response.ok(getKamerInfo(appService.neemDeel(kamerCode, deelnemerNaam))).build();
	}

	/**
	 * @return een JSON-string met de waarden {@code kamerCode} en {@code delenemerId}
	 */
	private String getKamerInfo(DeelnemerId deelnemerId) {
		var json = new JsonObject();
		json.addProperty("kamerCode", deelnemerId.getKamerCode());
		json.addProperty("deelnemerId", deelnemerId.getId());
		return json.toString();
	}

	@Inject
	void setAppService(AppService appService) {
		this.appService = appService;
	}

}
