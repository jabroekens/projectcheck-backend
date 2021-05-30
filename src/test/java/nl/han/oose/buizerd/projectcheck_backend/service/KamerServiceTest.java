package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerServiceTest {

	@Mock
	private DAO<Kamer, String> kamerDAO;

	private KamerService kamerService;

	@BeforeEach
	void setUp() {
		kamerService = new KamerService(kamerDAO);
	}

	@Nested
	class open_sluitVerbindingMetReden {

		private static final String KAMER_CODE = "123456";

		@Mock
		private Session session;

		@Mock
		private EndpointConfig config;

		void sluitVerbindingMetReden() throws IOException {
			CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kamer is gesloten of niet gevonden.");
			ArgumentCaptor<CloseReason> closeReasonCaptor = ArgumentCaptor.forClass(CloseReason.class);

			kamerService.open(session, config, KAMER_CODE);

			Mockito.verify(kamerDAO).read(Kamer.class, KAMER_CODE);
			Mockito.verify(session).close(closeReasonCaptor.capture());

			/*
			 * We moeten handmatig te relevante gegevens controleren omdat CloseReason
			 * niet zelf `equals` en `hashCode` implementeert maar deze erft van Object.
			 */
			Assertions.assertAll(
				() -> Assertions.assertEquals(closeReason.getCloseCode(), closeReasonCaptor.getValue().getCloseCode()),
				() -> Assertions.assertEquals(closeReason.getReasonPhrase(), closeReasonCaptor.getValue().getReasonPhrase())
			);
		}

		@Test
		void kamerAfwezig_sluitVerbindingMetReden() throws IOException {
			Mockito.when(kamerDAO.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.empty());
			sluitVerbindingMetReden();
		}

		@Test
		void kamerAanwezig_kamerFaseGesloten_sluitVerbindingMetReden(@Mock Kamer kamer) throws IOException {
			Mockito.when(kamerDAO.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
			Mockito.when(kamer.getKamerFase()).thenReturn(KamerFase.GESLOTEN);
			sluitVerbindingMetReden();
		}

	}

	@Nested
	class message {

		private static final String KAMER_CODE = "123456";

		@Mock
		private Event event;

		@Mock
		private Session session;

		@AfterEach
		void tearDown() {
			Mockito.verify(kamerDAO).read(Kamer.class, KAMER_CODE);
		}

		@Test
		void kamerAfwezig_stuurtEventResponse(@Mock RemoteEndpoint.Basic remote) throws IOException {
			EventResponse eventResponse = new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN)
				.metContext("kamerCode", KAMER_CODE).antwoordOp(event);

			Mockito.when(kamerDAO.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.empty());
			Mockito.when(session.getBasicRemote()).thenReturn(remote);

			kamerService.message(event, KAMER_CODE, session);

			Mockito.verify(remote).sendText(eventResponse.asJson());
		}

		@Test
		void kamerAanwezig_deelnemerAfwezig_stuurtEventResponse(@Mock Kamer kamer, @Mock RemoteEndpoint.Basic remote) throws IOException {
			EventResponse eventResponse = new EventResponse(EventResponse.Status.VERBODEN).antwoordOp(event);

			Mockito.when(kamerDAO.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
			Mockito.when(kamer.getDeelnemer(event.getDeelnemerId())).thenReturn(Optional.empty());
			Mockito.when(session.getBasicRemote()).thenReturn(remote);

			kamerService.message(event, KAMER_CODE, session);

			Mockito.verify(kamer).getDeelnemer(event.getDeelnemerId());
			Mockito.verify(remote).sendText(eventResponse.asJson());
		}

		@Test
		void kamerAanwezig_deelnemerAanwezig_voertEventUit(@Mock Kamer kamer, @Mock Deelnemer deelnemer) throws IOException {
			Mockito.when(kamerDAO.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
			Mockito.when(kamer.getDeelnemer(event.getDeelnemerId())).thenReturn(Optional.of(deelnemer));

			kamerService.message(event, KAMER_CODE, session);

			Mockito.verify(kamer).getDeelnemer(event.getDeelnemerId());
			Mockito.verify(event).voerUit(kamerDAO, deelnemer, session);
		}

	}

	@Nested
	class error {

		private static final String KAMER_CODE = "123456";

		@Mock
		private Session session;

		@Mock
		private RemoteEndpoint.Basic remoteEndpoint;

		// ThrowingSupplier omdat `RemoteEndpoint.Basic#sendText(String)` een IOException kan gooien
		private void metGemockteLogger(ThrowingSupplier<Throwable> supplier) throws Throwable {
			Logger logger = Mockito.mock(Logger.class);

			try (MockedStatic<Logger> mock = Mockito.mockStatic(Logger.class)) {
				mock.when(() -> Logger.getLogger(Mockito.anyString())).thenReturn(logger);
				Throwable throwable = supplier.get();
				Mockito.verify(logger).log(Level.SEVERE, throwable.getMessage(), throwable);
			}
		}

		@Test
		void logtBijThrowablesAndersDanIllegalArgumentException(@Mock Throwable error) throws Throwable {
			metGemockteLogger(() -> {
				kamerService.error(session, error, KAMER_CODE);
				return error;
			});
		}

		@Nested
		class instanceofIllegalArgumentException {

			@Mock
			private IllegalArgumentException error;

			@Test
			void stuurtEventResponse() throws IOException {
				Mockito.when(session.getBasicRemote()).thenReturn(remoteEndpoint);
				EventResponse eventResponse = new EventResponse(EventResponse.Status.INVALIDE);

				kamerService.error(session, error, KAMER_CODE);

				Mockito.verify(remoteEndpoint).sendText(eventResponse.asJson());
			}

			@Test
			void logtBijException() throws Throwable {
				metGemockteLogger(() -> {
					Mockito.when(session.getBasicRemote()).thenReturn(remoteEndpoint);
					Mockito.doThrow(IllegalArgumentException.class).when(remoteEndpoint).sendText(Mockito.anyString());

					kamerService.error(session, error, KAMER_CODE);
					return error;
				});
			}

		}

	}

}
