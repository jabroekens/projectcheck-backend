package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.exceptions.KamerNietGevondenExceptie;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

import java.util.Optional;

@Path("/")
public class AppService extends Application {

	@Inject
	private KamerRepository kamerRepository;

	@Inject
	private KamerService kamerService;

	@Inject
	private DAO<Deelnemer, DeelnemerId> deelnemerDAO;




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

	@Path("/kamer/neemdeel/{kamer-code}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response neemDeel(@PathParam("kamer-code") String kamerCode, @FormParam("deelnemerNaam") String deelnemerNaam) {
		Optional<Kamer> kamer = kamerRepository.get(kamerCode);
		System.out.println(deelnemerNaam);
		if (kamer.isPresent()) {
			Deelnemer deelnemer = new Deelnemer(new DeelnemerId(kamer.get().genereerDeelnemerId(), kamer.get().getKamerCode()), deelnemerNaam);
			deelnemerDAO.create(deelnemer);

			// TODO @Luka: injecten DAO voor deelnemer en aanmaken deelnemer met DAO (zie KamerRepository voor een voorbeeld)
			kamer.get().voegDeelnemerToe(deelnemer);
			return Response.ok().entity(deelnemer).build();
			//kamerRepository.add(kamer.get());
			// afhankelijk van hoe de bovenstaande methode wordt afgehandeld, wel of niet returnen en iets meegeven met een Response

		} else {
			throw new KamerNietGevondenExceptie(kamerCode);
		}


	}

}
