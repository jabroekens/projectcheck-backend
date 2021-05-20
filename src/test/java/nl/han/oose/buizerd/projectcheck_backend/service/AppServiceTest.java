package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.ws.rs.core.Response;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

	@Mock
	private DAO<Kamer, String> kamerDAO;

	private AppService appService;

	@BeforeEach
	void setUp() {
		appService = new AppService(kamerDAO);
	}

	@Test
	void getKamerInfo_geeftJuisteJson() {
		String kamerCode = "123456";
		Long deelnemerId = 12L;

		String expected = "{\"kamerCode\":\"" + kamerCode + "\",\"deelnemerId\":" + deelnemerId + "}";
		Assertions.assertEquals(expected, appService.getKamerInfo(kamerCode, deelnemerId));
	}

	/*
	 * Het is nodig een gemockte ResponseBuilder terug te geven, omdat er anders
	 * "ClassNotFoundException: Provider for jakarta.ws.rs.ext.RuntimeDelegate
	 * cannot be found" gegooit wordt.
	 */
	private void metGemockteResponseBuilder(Runnable runnable) {
		Response.ResponseBuilder responseBuilder = Mockito.mock(Response.ResponseBuilder.class);

		try (MockedStatic<Response> mock = Mockito.mockStatic(Response.class)) {
			mock.when(() -> Response.ok(Mockito.anyString())).thenReturn(responseBuilder);
			runnable.run();
			mock.verify(() -> Response.ok(Mockito.anyString()));
		}
	}

	@Nested
	class maakKamer {

		@Test
		void genereertCode() {
			String begeleiderNaam = "Joost";

			try (MockedStatic<Kamer> mock = Mockito.mockStatic(Kamer.class)) {
				mock.when(Kamer::genereerCode).thenThrow(RuntimeException.class);
				Assertions.assertThrows(RuntimeException.class, () -> appService.maakKamer(begeleiderNaam));
				mock.verify(Kamer::genereerCode);
			}
		}

		@Test
		void slaatKamerOp() {
			String kamerCode = "123456";
			String begeleiderNaam = "Joost";

			ArgumentCaptor<Kamer> kamerCaptor = ArgumentCaptor.forClass(Kamer.class);

			metGemockteResponseBuilder(() -> {
				appService.maakKamer(begeleiderNaam);

				Mockito.verify(kamerDAO).create(kamerCaptor.capture());
				Assertions.assertNotNull(kamerCaptor.getValue().getBegeleider());
				Assertions.assertEquals(begeleiderNaam, kamerCaptor.getValue().getBegeleider().getNaam());
			});
		}

	}

	@Nested
	class neemDeel {

		@Test
		void kamerAfwezig_gooitKamerNietGevondenException() {
			String kamerCode = "123456";
			String deelnemerNaam = "Joost";

			Mockito.when(kamerDAO.read(Kamer.class, kamerCode)).thenReturn(Optional.empty());
			Assertions.assertThrows(KamerNietGevondenException.class, () -> appService.neemDeel(kamerCode, deelnemerNaam));
			Mockito.verify(kamerDAO).read(Kamer.class, kamerCode);
		}

		@Nested
		class kamerAanwezig {

			@Test
			void kamerOpen_voegtDeelnemerToe(@Mock Kamer kamer) {
				String kamerCode = "123456";
				String deelnemerNaam = "Joost";

				ArgumentCaptor<Deelnemer> deelnemerCaptor = ArgumentCaptor.forClass(Deelnemer.class);

				metGemockteResponseBuilder(() -> {
					Mockito.when(kamerDAO.read(Kamer.class, kamerCode)).thenReturn(Optional.of(kamer));
					Mockito.when(kamer.getKamerFase()).thenReturn(KamerFase.OPEN);

					appService.neemDeel(kamerCode, deelnemerNaam);

					Mockito.verify(kamer).voegDeelnemerToe(deelnemerCaptor.capture());
					Mockito.verify(kamerDAO).update(kamer);
					Assertions.assertEquals(deelnemerNaam, deelnemerCaptor.getValue().getNaam());
				});
			}

			@Test
			void kamerNietOpen_gooitKamerGeslotenException(@Mock Kamer kamer, @Mock KamerFase kamerFase) {
				String kamerCode = "123456";
				String deelnemerNaam = "Joost";

				Mockito.when(kamerDAO.read(Kamer.class, kamerCode)).thenReturn(Optional.of(kamer));
				Mockito.when(kamer.getKamerFase()).thenReturn(kamerFase);

				Assertions.assertThrows(KamerGeslotenException.class, () -> appService.neemDeel(kamerCode, deelnemerNaam));

				Mockito.verify(kamerDAO).read(Kamer.class, kamerCode);
			}

		}

	}
	
}
