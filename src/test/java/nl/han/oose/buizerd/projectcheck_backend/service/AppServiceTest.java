package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.ws.rs.core.Response;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.repository.KamerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppServiceTest {

	@Mock
	private KamerRepository kamerRepository;

	@Mock
	private KamerService kamerService;

	@Captor
	private ArgumentCaptor<Deelnemer> deelnemerCaptor;

	private AppService appService;

	@BeforeEach
	void setUp() {
		appService = new AppService(kamerRepository, kamerService);
	}

	@Test
	void maakKamer_maaktKamer(@Mock Kamer kamer, @Mock Response.ResponseBuilder responseBuilder) {
		String begeleiderNaam = "Joost";
		Mockito.when(kamerRepository.maakKamer(begeleiderNaam)).thenReturn(kamer);

		try (MockedStatic<Response> mock = Mockito.mockStatic(Response.class)) {
			mock.when(() -> Response.ok(appService.getWebSocketURL(kamer.getKamerCode()))).thenReturn(responseBuilder);
			Assertions.assertEquals(responseBuilder.build(), appService.maakKamer(begeleiderNaam));
			Mockito.verify(kamerRepository).maakKamer(begeleiderNaam);
		}
	}

	@Test
	void maakKamer_registreerdKamer(@Mock Kamer kamer) {
		String begeleiderNaam = "Joost";
		Mockito.when(kamerRepository.maakKamer(begeleiderNaam)).thenReturn(kamer);

		try (MockedStatic<KamerService> mock = Mockito.mockStatic(KamerService.class)) {
			mock.when(() -> KamerService.registreer(kamer.getKamerCode())).thenThrow(new RuntimeException());
			Assertions.assertThrows(RuntimeException.class, () -> appService.maakKamer(begeleiderNaam));
			Mockito.verify(kamerRepository).maakKamer(begeleiderNaam);
		}
	}

	@Test
	void neemDeel_kamerAanwezig(@Mock Kamer kamer, @Mock Response.ResponseBuilder responseBuilder) {
		String kamerCode = "123456";
		String deelnemerNaam = "Joost";
		Long deelnemerId = kamer.genereerDeelnemerId();

		Mockito.when(kamerRepository.get(kamerCode)).thenReturn(Optional.of(kamer));

		try (MockedStatic<Response> mock = Mockito.mockStatic(Response.class)) {
			mock.when(() -> Response.ok(appService.getWebSocketInfo(kamer.getKamerCode(),deelnemerId))).thenReturn(responseBuilder);
			appService.neemDeel(kamerCode, deelnemerNaam);
			Mockito.verify(kamer).voegDeelnemerToe(deelnemerCaptor.capture());

			Assertions.assertAll(
				() -> Assertions.assertEquals(deelnemerNaam, deelnemerCaptor.getValue().getNaam()),
				() -> Assertions.assertEquals(deelnemerId, deelnemerCaptor.getValue().getDeelnemerId().getId()),
				() -> Assertions.assertEquals(kamer.getKamerCode(), deelnemerCaptor.getValue().getDeelnemerId().getKamerCode())
			);
		}
	}

	@Test
	void neemDeel_kamerAfwezig() {
		String kamerCode = "123456";
		String deelnemerNaam = "Joost";

		Mockito.when(kamerRepository.get(kamerCode)).thenReturn(Optional.empty());
		Assertions.assertThrows(KamerNietGevondenException.class, () -> appService.neemDeel(kamerCode, deelnemerNaam));
	}

	@Test
	void geeftJuisteWebSocketURL() {
		String kamerCode = "123456";
		Mockito.when(kamerService.getUrl(kamerCode)).thenReturn(kamerCode);

		String expected = "{\"kamer_url\":\"" + kamerCode + "\"}";
		Assertions.assertEquals(expected, appService.getWebSocketURL(kamerCode));
	}
	@Test
	void geeftJuisteDeelnemerId(){
		//Arrange
		String kamerCode = "123456";
		long deelnemerId = 12l;

		//Assert
		String expected = "{\"kamer_url\":null,\"deelnemer_id\":" + deelnemerId + "}";
		Assertions.assertEquals(expected, appService.getWebSocketInfo(kamerCode,deelnemerId));

	}

}
