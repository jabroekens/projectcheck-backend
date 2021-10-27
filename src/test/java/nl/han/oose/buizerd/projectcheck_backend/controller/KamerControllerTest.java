package nl.han.oose.buizerd.projectcheck_backend.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.service.KamerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerControllerTest {

	private static final String KAMER_CODE = "123456";

	private static Logger logger;

	@BeforeAll
	static void setUpAll() {
		logger = mock(Logger.class);
		KamerController.logger = logger;
	}

	@Mock
	private KamerService kamerService;

	@Mock
	private ManagedExecutorService managedExecutorService;

	private KamerController sut;

	@BeforeEach
	void setUp() {
		sut = new KamerController();
		sut.setKamerService(kamerService);
		sut.setManagedExecutorService(managedExecutorService);
	}

	@Test
	void open_kanNietDeelnemen_sluitVerbindingMetReden(@Mock Session session, @Mock EndpointConfig config)
	throws IOException {
		var closeReason = new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Kan niet deelnemen aan kamer.");
		var closeReasonCaptor = ArgumentCaptor.forClass(CloseReason.class);

		sut.open(session, config, KAMER_CODE);
		verify(kamerService).kanDeelnemen(KAMER_CODE);
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

	@Nested
	class message {

		@Mock
		private Event event;

		@Mock
		private Session session;

		@BeforeEach
		void setUp() {
			doAnswer((invocation -> {
				invocation.getArgument(0, Runnable.class).run();
				return null;
			})).when(managedExecutorService).execute(any());
		}

		@Test
		void deelnemerNietGevonden_stuurtEventResponse(@Mock RemoteEndpoint.Async remoteEndpoint) {
			when(event.voerUit(kamerService)).thenThrow(DeelnemerNietGevondenException.class);
			when(session.getAsyncRemote()).thenReturn(remoteEndpoint);

			sut.message(event, KAMER_CODE, session);

			verify(event).voerUit(kamerService);
			var eventResponse = new EventResponse(EventResponse.Status.VERBODEN).antwoordOp(event);
			verify(remoteEndpoint).sendText(eventResponse.asJson());
		}

		@Test
		void kamerNietGevonden_stuurtEventResponse(@Mock RemoteEndpoint.Async remoteEndpoint) {
			when(event.voerUit(kamerService)).thenThrow(KamerNietGevondenException.class);
			when(session.getAsyncRemote()).thenReturn(remoteEndpoint);

			sut.message(event, KAMER_CODE, session);

			verify(event).voerUit(kamerService);
			var eventResponse = new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN)
				.metContext("kamerCode", KAMER_CODE).antwoordOp(event);
			verify(remoteEndpoint).sendText(eventResponse.asJson());
		}

		@Nested
		class deelnemerEnKamerGevonden {

			@Test
			void voertEventUit_logtBijException(@Mock RuntimeException exception) {
				when(event.voerUit(kamerService)).thenThrow(exception);

				sut.message(event, KAMER_CODE, session);
				/*
				 * Het is niet mogelijk om te controleren of de error-methode aangeroepen wordt
				 * zonder de error-methode zelf te testen, wat al in een andere unit test
				 * gedaan wordt.
				 */
			}

			@Test
			void voertEventUit_isStuurNaarAlleClients_stuurtEventResponseNaarAlleOpenSessions(
				@Mock EventResponse eventResponse,
				@Mock RemoteEndpoint.Async remoteEndpoint
			) {
				when(event.voerUit(kamerService)).thenReturn(eventResponse);
				when(eventResponse.isStuurNaarAlleClients()).thenReturn(true);
				when(eventResponse.antwoordOp(event)).thenReturn(eventResponse);

				var openSessions = Set.of(mock(Session.class), mock(Session.class));
				when(session.getOpenSessions()).thenReturn(openSessions);
				openSessions.forEach(s -> {
					when(s.isOpen()).thenReturn(true);
					when(s.getAsyncRemote()).thenReturn(remoteEndpoint);
				});

				sut.message(event, KAMER_CODE, session);

				verify(event).voerUit(kamerService);
				verify(eventResponse).isStuurNaarAlleClients();
				verify(session).getOpenSessions();
				openSessions.forEach(s -> verify(s).isOpen());
				verify(eventResponse, times(openSessions.size())).antwoordOp(event);
				verify(remoteEndpoint, times(openSessions.size())).sendText(eventResponse.asJson());
			}

			@Test
			void voertEventUit_isNietStuurNaarAlleClients_stuurtEventResponseAlsSessionOpenIs(
				@Mock EventResponse eventResponse,
				@Mock RemoteEndpoint.Async remoteEndpoint
			) {
				when(event.voerUit(kamerService)).thenReturn(eventResponse);
				when(eventResponse.isStuurNaarAlleClients()).thenReturn(false);
				when(session.isOpen()).thenReturn(true);
				when(session.getAsyncRemote()).thenReturn(remoteEndpoint);
				when(eventResponse.antwoordOp(event)).thenReturn(eventResponse);

				sut.message(event, KAMER_CODE, session);

				verify(event).voerUit(kamerService);
				verify(eventResponse).isStuurNaarAlleClients();
				verify(session).isOpen();
				verify(eventResponse).antwoordOp(event);
				verify(remoteEndpoint).sendText(eventResponse.asJson());
			}

			@Test
			void voertEventUit_isNietStuurNaarAlleClients_doetNietsAlsSessionGeslotenIs(
				@Mock EventResponse eventResponse
			) {
				when(event.voerUit(kamerService)).thenReturn(eventResponse);
				when(eventResponse.isStuurNaarAlleClients()).thenReturn(false);
				when(session.isOpen()).thenReturn(false);

				sut.message(event, KAMER_CODE, session);

				verify(event).voerUit(kamerService);
				verify(eventResponse).isStuurNaarAlleClients();
				verify(session).isOpen();
				verifyNoMoreInteractions(event, session, eventResponse);
			}

		}

	}

	@Nested
	class error {

		@Mock
		private Session session;

		@Mock
		private RemoteEndpoint.Basic remoteEndpoint;

		@Test
		void paktExceptionUitAlsDezeNietNullIs(@Mock Throwable error) {
			sut.error(session, new Throwable(error), KAMER_CODE);
			verify(logger).log(any(), any(), eq(error));
		}

		@Test
		void logtBijThrowablesAndersDanIllegalArgumentException(@Mock Throwable error) {
			sut.error(session, error, KAMER_CODE);
			verify(logger).log(Level.SEVERE, error.getMessage(), error);
		}

		@Nested
		class instanceofIllegalArgumentException {

			@Mock
			private IllegalArgumentException error;

			@Test
			void stuurtEventResponse() throws IOException {
				var eventResponse = new EventResponse(EventResponse.Status.ONGELDIG);
				when(session.getBasicRemote()).thenReturn(remoteEndpoint);

				sut.error(session, error, KAMER_CODE);

				verify(remoteEndpoint).sendText(eventResponse.asJson());
			}

			@Test
			void logtBijException() throws Throwable {
				when(session.getBasicRemote()).thenReturn(remoteEndpoint);
				doThrow(error).when(remoteEndpoint).sendText(anyString());

				sut.error(session, error, KAMER_CODE);

				verify(logger).log(Level.SEVERE, error.getMessage(), error);
			}

		}

	}

}
