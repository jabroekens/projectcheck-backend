package nl.han.oose.buizerd.projectcheck_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerServiceImplTest {

	@Mock
	private DAO dao;

	private KamerServiceImpl sut;

	@BeforeEach
	void setUp() {
		sut = new KamerServiceImpl();
		sut.setDao(dao);
	}

	@Nested
	class kanDeelnemen {

		private static final String KAMER_CODE = "123456";

		@AfterEach
		void tearDown() {
			verify(dao).read(Kamer.class, KAMER_CODE);
		}

		@Test
		void kamerAfwezig_geeftFalseTerug() {
			when(dao.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.empty());
			assertFalse(sut.kanDeelnemen(KAMER_CODE));
		}

		@Test
		void kamerGesloten_gooitKamerGeslotenException(@Mock Kamer kamer) {
			when(dao.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
			when(kamer.getKamerFase()).thenReturn(KamerFase.GESLOTEN);

			assertFalse(sut.kanDeelnemen(KAMER_CODE));

			verify(kamer).getKamerFase();
		}

	}

	@Nested
	class voerEventUit {

		@Mock
		private Event event;

		@BeforeEach
		void setUp(@Mock DeelnemerId deelnemerId) {
			when(event.getDeelnemerId()).thenReturn(deelnemerId);
		}

		@AfterEach
		void tearDown() {
			verify(dao).read(Kamer.class, event.getDeelnemerId().getKamerCode());
		}

		@Test
		void kamerAfwezig_gooitKamerNietGevondenException() {
			when(dao.read(Kamer.class, event.getDeelnemerId().getKamerCode())).thenReturn(Optional.empty());
			assertThrows(KamerNietGevondenException.class, () -> sut.voerEventUit(event));
		}

		@Nested
		class kamerAanwezig {

			@Mock
			private Kamer kamer;

			@BeforeEach
			void setUp() {
				when(dao.read(Kamer.class, event.getDeelnemerId().getKamerCode())).thenReturn(Optional.of(kamer));
			}

			@AfterEach
			void tearDown() {
				verify(kamer).getDeelnemer(event.getDeelnemerId());
			}

			@Test
			void deelnemerAfwezig_gooitVerbodenToegangException() {
				when(kamer.getDeelnemer(event.getDeelnemerId())).thenReturn(Optional.empty());
				assertThrows(DeelnemerNietGevondenException.class, () -> sut.voerEventUit(event));
			}

			@Test
			void deelnemerAanwezig_voertEventUit(
				@Mock Deelnemer deelnemer,
				@Mock CompletableFuture<EventResponse> completableFuture
			) {
				when(kamer.getDeelnemer(event.getDeelnemerId())).thenReturn(Optional.of(deelnemer));
				when(event.voerUit(dao, deelnemer)).thenReturn(completableFuture);

				assertEquals(completableFuture, sut.voerEventUit(event));

				verify(event).voerUit(dao, deelnemer);
			}

		}

	}

}
