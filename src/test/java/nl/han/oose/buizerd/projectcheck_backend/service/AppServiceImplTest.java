package nl.han.oose.buizerd.projectcheck_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.CodeGenerator;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppServiceImplTest {

	@Mock
	private DAO dao;

	@Mock
	private CodeGenerator codeGenerator;

	private AppServiceImpl sut;

	@BeforeEach
	void setUp() {
		sut = new AppServiceImpl();
		sut.setDao(dao);
		sut.setCodeGenerator(codeGenerator);
	}

	@Nested
	class maakKamer {

		private static final String BEGELEIDER_NAAM = "Joost";

		@Test
		void genereertCode() {
			sut.maakKamer(BEGELEIDER_NAAM);
			verify(codeGenerator).genereerCode(anyInt());
		}

		@Test
		void maaktBegeleiderAanMetJuisteWaardenEnSlaatKamerOp() {
			var kamerCode = "123456";
			var kamerCaptor = ArgumentCaptor.forClass(Kamer.class);

			sut.maakKamer(BEGELEIDER_NAAM);

			verify(dao).create(kamerCaptor.capture());
			assertNotNull(kamerCaptor.getValue().getBegeleider());
			assertEquals(BEGELEIDER_NAAM, kamerCaptor.getValue().getBegeleider().getNaam());
		}

	}

	@Nested
	class neemDeel {

		private static final String KAMER_CODE = "123456";
		private static final String DEELNEMER_NAAM = "Joost";

		@Test
		void kamerAfwezig_gooitKamerNietGevondenException() {
			when(dao.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.empty());
			assertThrows(KamerNietGevondenException.class, () -> sut.neemDeel(KAMER_CODE, DEELNEMER_NAAM));
			verify(dao).read(Kamer.class, KAMER_CODE);
		}

		@Nested
		class kamerAanwezig {

			@Mock
			private Kamer kamer;

			@Test
			void kamerOpen_voegtDeelnemerMetJuisteWaardenToe() {
				var deelnemerCaptor = ArgumentCaptor.forClass(Deelnemer.class);

				when(dao.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
				when(kamer.getKamerFase()).thenReturn(KamerFase.OPEN);

				sut.neemDeel(KAMER_CODE, DEELNEMER_NAAM);

				verify(kamer).addDeelnemer(deelnemerCaptor.capture());
				verify(dao).update(kamer);
				assertEquals(DEELNEMER_NAAM, deelnemerCaptor.getValue().getNaam());
			}

			@Test
			void kamerNietOpen_gooitKamerGeslotenException(@Mock KamerFase kamerFase) {
				when(dao.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
				when(kamer.getKamerFase()).thenReturn(kamerFase);

				assertThrows(KamerGeslotenException.class, () -> sut.neemDeel(KAMER_CODE, DEELNEMER_NAAM));
				verify(dao).read(Kamer.class, KAMER_CODE);
			}

		}

	}

}
