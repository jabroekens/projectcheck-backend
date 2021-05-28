package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.Session;
import jakarta.ws.rs.core.UriInfo;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KamerServiceTest {

	@Mock
	private UriInfo uriInfo;

	@Mock
	private DAO<Kamer, String> kamerDAO;

	private KamerService kamerService;

	@BeforeEach
	void setUp() {
		kamerService = new KamerService(uriInfo, kamerDAO);
	}

	@AfterEach
	void tearDown() {
		KamerService.GEREGISTREERDE_KAMERS.clear();
	}

	@Test
	void instantieertStatischeVelden() {
		Assertions.assertNotNull(KamerService.LOGGER);
		Assertions.assertNotNull(KamerService.GEREGISTREERDE_KAMERS);
	}

	@Test
	void registreertKamerCode() {
		String kamerCode = "123456";
		KamerService.registreer(kamerCode);
		Assertions.assertTrue(KamerService.GEREGISTREERDE_KAMERS.contains(kamerCode));
	}

	/*
	 * Kan op dit moment niet getest worden; UriBuilder is een interface van het
	 * Builder-pattern, wat betekent dat de aangeroepen methodes de instantie
	 * van Builder zelf horen te returnen. Echter is het zo dat Mockito bij
	 * de gemockte UriBuilder altijd `null` teruggeeft op de relevante
	 * methoden met als gevolg een NullPointerException als de unit tests
	 * uitgevoerd worden.
	 */
	// @Test
	// void geeftJuistURL_http(@Mock UriBuilder uriBuilder) {
	// 	URI uri = Mockito.mock(URI.class, Mockito.withSettings().useConstructor("http://localhost"));
	// 	Mockito.when(uriInfo.getBaseUri()).thenReturn(uri);
	//
	// 	String kamerCode = "123456";
	//
	// 	try (MockedStatic<UriBuilder> mock = Mockito.mockStatic(UriBuilder.class)) {
	// 		mock.when(() -> UriBuilder.fromUri(uri)).thenReturn(uriBuilder);
	// 		Assertions.assertEquals("ws://localhost/kamer/" + kamerCode, kamerService.getUrl(kamerCode));
	// 	}
	// }
	//
	// @Test
	// void geeftJuistURL_https(@Mock UriBuilder uriBuilder) {
	// 	URI uri = Mockito.mock(URI.class, Mockito.withSettings().useConstructor("https://localhost"));
	// 	Mockito.when(uriInfo.getBaseUri()).thenReturn(uri);
	//
	// 	String kamerCode = "123456";
	//
	// 	try (MockedStatic<UriBuilder> mock = Mockito.mockStatic(UriBuilder.class)) {
	// 		mock.when(() -> UriBuilder.fromUri(uri)).thenReturn(uriBuilder);
	// 		Assertions.assertEquals("wss://localhost/kamer/" + kamerCode, kamerService.getUrl(kamerCode));
	// 	}
	// }

	@Test
	void open_geregistreerdeKamerCode(@Mock Session session, @Mock EndpointConfig config) {
		String kamerCode = "123456";
		KamerService.registreer(kamerCode);
		Assertions.assertDoesNotThrow(() -> kamerService.open(session, config, kamerCode));
	}

	/*
	 * Kan op dit moment niet getest worden, omdat er een CloseReason-instantie
	 * aangemaakt wordt binnen `KamerService#open(Session, EndpointConfig, String)`
	 * en deze niet gemockt kan worden. CloseReason implementeert zelf geen eigen
	 * `equals` methode en erft deze van Object, die alleen vergelijk of `a == b`.
	 * Dit gooit een ArgumentsAreDifferent-exceptie bij het uitvoeren van de tests.
	 */
	// @Test
	// void open_kamerNietGevonden(@Mock Session session, @Mock EndpointConfig config) {
	// 	String kamerCode = "123456";
	// 	CloseReason closeReason = Mockito.spy(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kamer niet gevonden"));
	//
	// 	Assertions.assertAll(
	// 		() -> {
	// 			kamerService.open(session, config, kamerCode);
	// 			Mockito.verify(session).close(closeReason);
	// 		},
	// 		() -> {
	// 			Exception exception = new IOException();
	// 			Mockito.doThrow(exception).when(session).close(closeReason);
	// 			Mockito.verify(KamerService.LOGGER).log(Level.SEVERE, exception.getMessage(), exception);
	// 		}
	// 	);
	// }

	@Test
	void close_DoetNiets(@Mock Session session, @Mock CloseReason closeReason) {
		String kamerCode = "123456";
		kamerService.close(session, closeReason, kamerCode);
		Mockito.verifyNoInteractions(session, closeReason);
	}

}
