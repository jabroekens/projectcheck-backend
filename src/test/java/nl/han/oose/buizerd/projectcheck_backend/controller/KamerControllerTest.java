package nl.han.oose.buizerd.projectcheck_backend.controller;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jakarta.websocket.CloseReason;
import jakarta.websocket.EndpointConfig;
import jakarta.websocket.RemoteEndpoint;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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

	private KamerController sut;

	@BeforeEach
	void setUp() {
		sut = new KamerController();
		sut.setKamerService(kamerService);
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

		@Test
		void kamerAanwezig_verbodenToegang_stuurtEventResponse(@Mock RemoteEndpoint.Basic remote)
		throws IOException {
			var eventResponse = new EventResponse(EventResponse.Status.VERBODEN).antwoordOp(event);
			when(kamerService.voerEventUit(event)).thenThrow(DeelnemerNietGevondenException.class);
			when(session.getBasicRemote()).thenReturn(remote);

			sut.message(event, KAMER_CODE, session);

			verify(remote).sendText(eventResponse.asJson());
		}

		@Test
		void kamerAfwezig_stuurtEventResponse(@Mock RemoteEndpoint.Basic remote) throws IOException {
			var eventResponse = new EventResponse(EventResponse.Status.KAMER_NIET_GEVONDEN)
				.metContext("kamerCode", KAMER_CODE).antwoordOp(event);
			when(kamerService.voerEventUit(event)).thenThrow(KamerNietGevondenException.class);
			when(session.getBasicRemote()).thenReturn(remote);

			sut.message(event, KAMER_CODE, session);

			verify(remote).sendText(eventResponse.asJson());
		}

		@Nested
		class kamerAanwezig_deelnemerAanwezig {

			@Test
			void voertEventUit(@Mock CompletableFuture<EventResponse> completableFuture) throws IOException {
				when(kamerService.voerEventUit(event)).thenReturn(completableFuture);
				sut.message(event, KAMER_CODE, session);
				verify(kamerService).voerEventUit(event);
			}

			@Test
			void voertEventUit_logtBijException(@Mock Throwable throwable)
			throws IOException {
				when(kamerService.voerEventUit(event)).thenReturn(CompletableFuture.failedFuture(throwable));
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
			) throws IOException {
				when(kamerService.voerEventUit(event)).thenReturn(CompletableFuture.supplyAsync(() -> eventResponse));
				when(eventResponse.isStuurNaarAlleClients()).thenReturn(true);

				var openSessions = spy(Set.of(mock(Session.class), mock(Session.class)));
				openSessions.forEach(s -> {
					when(s.isOpen()).thenReturn(true);
					when(s.getAsyncRemote()).thenReturn(remoteEndpoint);
				});
				when(session.getOpenSessions()).thenReturn(openSessions);

				sut.message(event, KAMER_CODE, session);

				verify(remoteEndpoint, times(2)).sendText(eventResponse.asJson());
			}

			@Test
			void voertEventUit_isNietStuurNaarAlleClients_stuurtEventResponseAlsSessionOpenIs(
				@Mock EventResponse eventResponse,
				@Mock RemoteEndpoint.Async remoteEndpoint
			) throws IOException {
				when(kamerService.voerEventUit(event)).thenReturn(CompletableFuture.supplyAsync(() -> eventResponse));
				when(eventResponse.isStuurNaarAlleClients()).thenReturn(false);
				when(session.isOpen()).thenReturn(true);
				when(session.getAsyncRemote()).thenReturn(remoteEndpoint);

				sut.message(event, KAMER_CODE, session);

				verify(remoteEndpoint).sendText(eventResponse.asJson());
				verifyNoMoreInteractions(session);
			}

			@Test
			void voertEventUit_isNietStuurNaarAlleClients_doetNietsAlsSessionGeslotenIs(
				@Mock EventResponse eventResponse
			) throws IOException {
				when(kamerService.voerEventUit(event)).thenReturn(CompletableFuture.supplyAsync(() -> eventResponse));
				when(eventResponse.isStuurNaarAlleClients()).thenReturn(false);
				when(session.isOpen()).thenReturn(false);

				sut.message(event, KAMER_CODE, session);

				verifyNoMoreInteractions(session);
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
