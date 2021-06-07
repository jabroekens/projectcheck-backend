package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.CodeGenerator;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

@Path("/")
public class AppService {

	private DAO dao;
	private CodeGenerator codeGenerator;

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @return een {@link Response OK Response} met daarin de kamercode van de aangemaakte kamer
	 *         en het ID van de begeleider in JSON
	 */
	@Path("kamer/nieuw")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response maakKamer(@FormParam("begeleiderNaam") @Naam String begeleiderNaam) {
		var kamerCode = codeGenerator.genereerCode(Kamer.KAMER_CODE_MAX_LENGTE);

		var deelnemerId = new DeelnemerId(1L, kamerCode);
		var begeleider = new Begeleider(deelnemerId, begeleiderNaam);
		var kamer = new Kamer(kamerCode, begeleider);

		dao.create(kamer);
		return Response.ok(getKamerInfo(kamer.getKamerCode(), deelnemerId.getId())).build();
	}

	/**
	 * Voegt een deelnemer met de naam {@code deelnemerNaam} toe aan de kamer met kamercode {@code kamerCode}
	 * als de kamer bestaat en open is.
	 */
	@Path("kamer/neemdeel/{kamerCode}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response neemDeel(@PathParam("kamerCode") @KamerCode String kamerCode, @FormParam("deelnemerNaam") @Naam String deelnemerNaam) {
		var kamer = dao.read(Kamer.class, kamerCode);

		if (kamer.isPresent()) {
			if (kamer.get().getKamerFase() != KamerFase.OPEN) {
				throw new KamerGeslotenException(kamerCode);
			}

			var deelnemerId = kamer.get().genereerDeelnemerId();
			var deelnemer = new Deelnemer(
				new DeelnemerId(deelnemerId, kamer.get().getKamerCode()),
				deelnemerNaam
			);

			kamer.get().addDeelnemer(deelnemer);
			dao.update(kamer.get());
			return Response.ok(getKamerInfo(kamerCode, deelnemerId)).build();
		} else {
			throw new KamerNietGevondenException(kamerCode);
		}
	}

	/**
	 * @return een JSON-string met de waarden {@code kamerCode} en {@code delenemerId}
	 */
	public String getKamerInfo(String kamerCode, Long deelnemerId) {
		var json = new JsonObject();
		json.addProperty("kamerCode", kamerCode);
		json.addProperty("deelnemerId", deelnemerId);
		return json.toString();
	}

	@Inject
	void setDao(DAO dao) {
		this.dao = dao;
	}

	@Inject
	void setCodeGenerator(CodeGenerator codeGenerator) {
		this.codeGenerator = codeGenerator;
	}

}
