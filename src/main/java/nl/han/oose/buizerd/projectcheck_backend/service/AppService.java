package nl.han.oose.buizerd.projectcheck_backend.service;

import com.google.gson.JsonObject;
import jakarta.inject.Inject;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.Naam;

@Path("/")
public class AppService extends Application {

	@Inject
	private DAO<Kamer, String> kamerDAO;

	@Inject
	private KamerService kamerService;

	/**
	 * Construeert een {@link AppService}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JAX-RS en mag niet aangeroepen worden.</b>
	 */
	public AppService() {
	}

	/**
	 * Construeert een {@link AppService}.
	 *
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 *
	 * @param kamerDAO Een {@link DAO} voor {@link Kamer}.
	 * @param kamerService Een {@link KamerService}.
	 */
	AppService(DAO<Kamer, String> kamerDAO, KamerService kamerService) {
		this.kamerDAO = kamerDAO;
		this.kamerService = kamerService;
	}

	/**
	 * Maakt een een kamer aan onder begeleiding van een begeleider genaamd {@code begeleiderNaam}.
	 *
	 * @param begeleiderNaam De naam van de {@link Begeleider}.
	 * @return Een JSON string met de WebSocket URL van de {@link Kamer} gewikkelt in {@link Response}.
	 */
	@Path("/kamer/nieuw")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response maakKamer(@FormParam("begeleiderNaam") @Naam String begeleiderNaam) {
		String kamerCode = Kamer.genereerCode();

		Begeleider begeleider = new Begeleider(new DeelnemerId(1L, kamerCode), begeleiderNaam);
		Kamer kamer = new Kamer(kamerCode, begeleider);

		kamerDAO.create(kamer);
		KamerService.registreer(kamer.getKamerCode());

		return Response.ok(getWebSocketURL(kamer.getKamerCode())).build();
	}

	@Path("/kamer/neemdeel/{kamerCode}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response neemDeel(@PathParam("kamerCode") @KamerCode String kamerCode, @FormParam("deelnemerNaam") @Naam String deelnemerNaam) {
		Optional<Kamer> kamer = kamerDAO.read(Kamer.class, kamerCode);

		if (kamer.isPresent()) {
			if (kamer.get().getKamerFase() != KamerFase.OPEN) {
				throw new KamerGeslotenException(kamerCode);
			}

			Long deelnemerId = kamer.get().genereerDeelnemerId();
			Deelnemer deelnemer = new Deelnemer(
				new DeelnemerId(deelnemerId, kamer.get().getKamerCode()),
				deelnemerNaam
			);

			kamer.get().voegDeelnemerToe(deelnemer);
			kamerDAO.update(kamer.get());
			return Response.ok(getWebSocketInfo(kamer.get().getKamerCode(), deelnemerId)).build();
		} else {
			throw new KamerNietGevondenException(kamerCode);
		}
	}

	String getWebSocketURL(String kamerCode) {
		JsonObject json = new JsonObject();
		json.addProperty("kamer_url", kamerService.getUrl(kamerCode));
		return json.toString();
	}
	String getWebSocketInfo(String kamerCode,Long deelnemerId) {
		JsonObject json = new JsonObject();
		json.addProperty("kamer_url", kamerService.getUrl(kamerCode));
		json.addProperty("deelnemer_id", deelnemerId);
		return json.toString();
	}

}
