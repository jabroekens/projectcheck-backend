package nl.han.oose.buizerd.projectcheck_backend.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerServiceTest {

	private static final String KAMER_CODE = "123456";

	private static Logger logger;

	@BeforeAll
	static void setUpAll() {
		KamerServiceTest.logger = mock(Logger.class);
		KamerService.logger = KamerServiceTest.logger;
	}

	@Mock
	private DAO dao;

	private KamerService sut;

	@BeforeEach
	void setUp() {
		sut = new KamerService(dao);
	}

	@Nested
	class open_sluitVerbindingMetReden {

		@Mock
		private Session session;

		@Mock
		private EndpointConfig config;

		void sluitVerbindingMetReden() throws IOException {
			CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kamer is gesloten of niet gevonden.");
			ArgumentCaptor<CloseReason> closeReasonCaptor = ArgumentCaptor.forClass(CloseReason.class);

			sut.open(session, config, KamerServiceTest.KAMER_CODE);

			verify(dao).read(Kamer.class, KamerServiceTest.KAMER_CODE);
			verify(session).close(closeReasonCaptor.capture());

			/*
			 * We moeten handmatig te relevante gegevens controleren omdat CloseReason
			 * niet zelf `equals` en `hashCode` implementeert maar deze erft van Object.
			 */
			assertAll(
				() -> assertEquals(closeReason.getCloseCode(), closeReasonCaptor.getValue().getCloseCode()),
				() -> assertEquals(closeReason.getReasonPhrase(), closeReasonCaptor.getValue().getReasonPhrase())
			);
		}

		@Test
		void kamerAfwezig_sluitVerbindingMetReden() throws IOException {
			when(dao.read(Kamer.class, KamerServiceTest.KAMER_CODE)).thenReturn(Optional.empty());
			sluitVerbindingMetReden();
		}

		@Test
		void kamerAanwezig_kamerFaseGesloten_sluitVerbindingMetReden(@Mock Kamer kamer) throws IOException {
			when(dao.read(Kamer.class, KamerServiceTest.KAMER_CODE)).thenReturn(Optional.of(kamer));
			when(kamer.getKamerFase()).thenReturn(KamerFase.GESLOTEN);
			sluitVerbindingMetReden();
		}

	}

	@Nested
	class message {

		@Mock
		private Event event;

		@Mock
		private Session session;

		@AfterEach
		void tearDown() {
			verify(dao).read(Kamer.class, KamerServiceTest.KAMER_CODE);
		}

		@Test
		void kamerAfwezig_stuurtEventResponse(@Mock RemoteEndpoint.Basic remote) throws IOException {
			EventResponse eventResponse = new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN)
				.metContext("kamerCode", KamerServiceTest.KAMER_CODE).antwoordOp(event);

			when(dao.read(Kamer.class, KamerServiceTest.KAMER_CODE)).thenReturn(Optional.empty());
			when(session.getBasicRemote()).thenReturn(remote);

			sut.message(event, KamerServiceTest.KAMER_CODE, session);

			verify(remote).sendText(eventResponse.asJson());
		}

		@Test
		void kamerAanwezig_deelnemerAfwezig_stuurtEventResponse(@Mock Kamer kamer, @Mock RemoteEndpoint.Basic remote) throws IOException {
			EventResponse eventResponse = new EventResponse(EventResponse.Status.VERBODEN).antwoordOp(event);

			when(dao.read(Kamer.class, KamerServiceTest.KAMER_CODE)).thenReturn(Optional.of(kamer));
			when(kamer.getDeelnemer(event.getDeelnemerId())).thenReturn(Optional.empty());
			when(session.getBasicRemote()).thenReturn(remote);

			sut.message(event, KamerServiceTest.KAMER_CODE, session);

			verify(kamer).getDeelnemer(event.getDeelnemerId());
			verify(remote).sendText(eventResponse.asJson());
		}

		@Test
		void kamerAanwezig_deelnemerAanwezig_voertEventUit(@Mock Kamer kamer, @Mock Deelnemer deelnemer) throws IOException {
			when(dao.read(Kamer.class, KamerServiceTest.KAMER_CODE)).thenReturn(Optional.of(kamer));
			when(kamer.getDeelnemer(event.getDeelnemerId())).thenReturn(Optional.of(deelnemer));

			sut.message(event, KamerServiceTest.KAMER_CODE, session);

			verify(kamer).getDeelnemer(event.getDeelnemerId());
			verify(event).voerUit(dao, deelnemer, session);
		}

	}

	@Nested
	class error {

		@Mock
		private Session session;

		@Mock
		private RemoteEndpoint.Basic remoteEndpoint;

		@Test
		void logtBijThrowablesAndersDanIllegalArgumentException(@Mock Throwable error) {
			sut.error(session, error, KamerServiceTest.KAMER_CODE);
			verify(KamerServiceTest.logger).log(Level.SEVERE, error.getMessage(), error);
		}

		@Nested
		class instanceofIllegalArgumentException {

			@Mock
			private IllegalArgumentException error;

			@Test
			void stuurtEventResponse() throws IOException {
				when(session.getBasicRemote()).thenReturn(remoteEndpoint);
				EventResponse eventResponse = new EventResponse(EventResponse.Status.INVALIDE);

				sut.error(session, error, KamerServiceTest.KAMER_CODE);

				verify(remoteEndpoint).sendText(eventResponse.asJson());
			}

			@Test
			void logtBijException() throws Throwable {
				when(session.getBasicRemote()).thenReturn(remoteEndpoint);
				doThrow(IllegalArgumentException.class).when(remoteEndpoint).sendText(anyString());

				sut.error(session, error, KamerServiceTest.KAMER_CODE);
				verify(KamerServiceTest.logger).log(Level.SEVERE, error.getMessage(), error);
			}

		}

	}

}
