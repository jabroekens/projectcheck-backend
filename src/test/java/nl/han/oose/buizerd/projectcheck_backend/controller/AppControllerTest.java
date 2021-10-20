package nl.han.oose.buizerd.projectcheck_backend.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.service.AppService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppControllerTest {

	@Mock
	private AppService appService;

	private AppController sut;

	@BeforeEach
	void setUp() {
		sut = new AppController();
		sut.setAppService(appService);
	}

	/*
	 * Het is nodig een gemockte ResponseBuilder terug te geven, omdat er anders
	 * "ClassNotFoundException: Provider for jakarta.ws.rs.ext.RuntimeDelegate
	 * cannot be found" gegooit wordt.
	 */
	private void metGemockteResponseBuilder(Runnable runnable) {
		var responseBuilder = mock(Response.ResponseBuilder.class);

		try (MockedStatic<Response> mock = mockStatic(Response.class)) {
			mock.when(() -> Response.ok(anyString())).thenReturn(responseBuilder);
			runnable.run();
			mock.verify(() -> Response.ok(anyString()));
		}
	}

	@Test
	void maakKamer_roeptServiceAanEnGeeftKamerInfoTerug(@Mock DeelnemerId deelnemerId) {
		var begeleiderNaam = "Joost";

		metGemockteResponseBuilder(() -> {
			doReturn(deelnemerId).when(appService).maakKamer(begeleiderNaam);
			sut.maakKamer(begeleiderNaam);
			verify(appService).maakKamer(begeleiderNaam);
		});
	}

	@Test
	void neemDeel_roeptServiceAanEnGeeftKamerInfoTerug(@Mock DeelnemerId deelnemerId) {
		var kamerCode = "123456";
		var deelnemerNaam = "Joost";

		metGemockteResponseBuilder(() -> {
			doReturn(deelnemerId).when(appService).neemDeel(kamerCode, deelnemerNaam);
			sut.neemDeel(kamerCode, deelnemerNaam);
			verify(appService).neemDeel(kamerCode, deelnemerNaam);
		});
	}

}
