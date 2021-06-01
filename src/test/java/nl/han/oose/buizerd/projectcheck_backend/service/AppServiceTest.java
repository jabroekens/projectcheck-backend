package nl.han.oose.buizerd.projectcheck_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.core.Response;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppServiceTest {

	@Mock
	private DAO dao;

	private AppService sut;

	@BeforeEach
	void setUp() {
		sut = new AppService(dao);
	}

	@Test
	void getKamerInfo_geeftJuisteJson() {
		String kamerCode = "123456";
		Long deelnemerId = 12L;

		String expected = "{\"kamerCode\":\"" + kamerCode + "\",\"deelnemerId\":" + deelnemerId + "}";
		assertEquals(expected, sut.getKamerInfo(kamerCode, deelnemerId));
	}

	/*
	 * Het is nodig een gemockte ResponseBuilder terug te geven, omdat er anders
	 * "ClassNotFoundException: Provider for jakarta.ws.rs.ext.RuntimeDelegate
	 * cannot be found" gegooit wordt.
	 */
	private void metGemockteResponseBuilder(Runnable runnable) {
		Response.ResponseBuilder responseBuilder = mock(Response.ResponseBuilder.class);

		try (MockedStatic<Response> mock = mockStatic(Response.class)) {
			mock.when(() -> Response.ok(anyString())).thenReturn(responseBuilder);
			runnable.run();
			mock.verify(() -> Response.ok(anyString()));
		}
	}

	@Nested
	class maakKamer {

		@Test
		void genereertCode() {
			String begeleiderNaam = "Joost";

			try (MockedStatic<Kamer> mock = mockStatic(Kamer.class)) {
				mock.when(Kamer::genereerCode).thenThrow(RuntimeException.class);
				assertThrows(RuntimeException.class, () -> sut.maakKamer(begeleiderNaam));
				mock.verify(Kamer::genereerCode);
			}
		}

		@Test
		void maaktBegeleiderAanMetJuisteWaardenEnSlaatKamerOp() {
			String kamerCode = "123456";
			String begeleiderNaam = "Joost";

			ArgumentCaptor<Kamer> kamerCaptor = ArgumentCaptor.forClass(Kamer.class);

			metGemockteResponseBuilder(() -> {
				sut.maakKamer(begeleiderNaam);

				verify(dao).create(kamerCaptor.capture());
				assertNotNull(kamerCaptor.getValue().getBegeleider());
				assertEquals(begeleiderNaam, kamerCaptor.getValue().getBegeleider().getNaam());
			});
		}

	}

	@Nested
	class neemDeel {

		@Test
		void kamerAfwezig_gooitKamerNietGevondenException() {
			String kamerCode = "123456";
			String deelnemerNaam = "Joost";

			when(dao.read(Kamer.class, kamerCode)).thenReturn(Optional.empty());
			assertThrows(KamerNietGevondenException.class, () -> sut.neemDeel(kamerCode, deelnemerNaam));
			verify(dao).read(Kamer.class, kamerCode);
		}

		@Nested
		class kamerAanwezig {

			@Mock
			private Kamer kamer;

			@Test
			void kamerOpen_voegtDeelnemerMetJuisteWaardenToe() {
				String kamerCode = "123456";
				String deelnemerNaam = "Joost";

				ArgumentCaptor<Deelnemer> deelnemerCaptor = ArgumentCaptor.forClass(Deelnemer.class);

				metGemockteResponseBuilder(() -> {
					when(dao.read(Kamer.class, kamerCode)).thenReturn(Optional.of(kamer));
					when(kamer.getKamerFase()).thenReturn(KamerFase.OPEN);

					sut.neemDeel(kamerCode, deelnemerNaam);

					verify(kamer).voegDeelnemerToe(deelnemerCaptor.capture());
					verify(dao).update(kamer);
					assertEquals(deelnemerNaam, deelnemerCaptor.getValue().getNaam());
				});
			}

			@Test
			void kamerNietOpen_gooitKamerGeslotenException(@Mock KamerFase kamerFase) {
				String kamerCode = "123456";
				String deelnemerNaam = "Joost";

				when(dao.read(Kamer.class, kamerCode)).thenReturn(Optional.of(kamer));
				when(kamer.getKamerFase()).thenReturn(kamerFase);

				assertThrows(KamerGeslotenException.class, () -> sut.neemDeel(kamerCode, deelnemerNaam));

				verify(dao).read(Kamer.class, kamerCode);
			}

		}

	}

}
