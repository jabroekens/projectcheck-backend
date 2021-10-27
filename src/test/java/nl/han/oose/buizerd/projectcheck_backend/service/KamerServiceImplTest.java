package nl.han.oose.buizerd.projectcheck_backend.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSelectie;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.Ronde;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;
import nl.han.oose.buizerd.projectcheck_backend.dto.DeelnemerKaartenSets;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerHeeftGeenRolException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KaartenSelectieVolException;
import nl.han.oose.buizerd.projectcheck_backend.exception.RondeNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
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

	@Test
	void getRelevanteRollen_geeftJuisteWaarden(@Mock Kamer kamer, @Mock Deelnemer deelnemer) {
		var kamerCode = "123456";
		var rollen = Set.of(mock(Rol.class));
		when(kamer.getRelevanteRollen()).thenReturn(rollen);
		when(dao.read(Kamer.class, kamerCode)).thenReturn(Optional.of(kamer));

		assertIterableEquals(rollen, sut.getRelevanteRollen(kamerCode));
		verify(kamer).getRelevanteRollen();
	}

	@Test
	void getStandaardRollen_geeftJuisteWaarden() {
		var expected = Arrays.stream(StandaardRol.values()).map(StandaardRol::getRol).collect(Collectors.toSet());
		var actual = sut.getStandaardRollen();
		assertIterableEquals(expected, actual);
	}

	@Test
	void kiesRelevanteRollen_deelnemerIsBegeleider_zetRelevanteRollen(
		@Mock DeelnemerId deelnemerId,
		@Mock Set<Rol> relevanteRollen,
		@Mock Kamer kamer,
		@Mock Begeleider begeleider
	) {
		when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
		when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(begeleider));

		sut.kiesRelevanteRollen(deelnemerId, relevanteRollen);

		verify(kamer).setRelevanteRollen(relevanteRollen);
	}

	@Test
	void kiesRelevanteRollen_deelnemerIsGeenBegeleider_gooitVerbodenToegangException(
		@Mock DeelnemerId deelnemerId,
		@Mock Set<Rol> relevanteRollen,
		@Mock Kamer kamer,
		@Mock Deelnemer deelnemer
	) {
		when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
		when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(deelnemer));

		assertThrows(VerbodenToegangException.class, () -> sut.kiesRelevanteRollen(deelnemerId, relevanteRollen));
	}

	@Test
	void kiesRol_zetRol(@Mock DeelnemerId deelnemerId, @Mock Rol rol, @Mock Kamer kamer, @Mock Deelnemer deelnemer) {
		when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
		when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(deelnemer));

		sut.kiesRol(deelnemerId, rol);

		verify(deelnemer).setRol(rol);
	}

	@Test
	void naarVolgendeFase_deelnemerIsBegeleider_zetVolgendeFaseEnGeeftDezeTerug(
		@Mock DeelnemerId deelnemerId,
		@Mock Kamer kamer,
		@Mock Begeleider begeleider,
		@Mock KamerFase kamerFase,
		@Mock KamerFase volgendeFase
	) {
		when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
		when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(begeleider));
		when(kamer.getKamerFase()).thenReturn(kamerFase);
		when(kamerFase.getVolgendeFase()).thenReturn(volgendeFase);

		assertEquals(volgendeFase, sut.naarVolgendeFase(deelnemerId));
		verify(kamerFase).getVolgendeFase();
		verify(kamer).setKamerFase(volgendeFase);
	}

	@Test
	void naarVolgendeFase_deelnemerIsGeenBegeleider_gooitVerbodenToegangException(
		@Mock DeelnemerId deelnemerId,
		@Mock Kamer kamer,
		@Mock Deelnemer deelnemer,
		@Mock KamerFase kamerFase,
		@Mock KamerFase volgendeFase
	) {
		when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
		when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(deelnemer));
		assertThrows(VerbodenToegangException.class, () -> sut.naarVolgendeFase(deelnemerId));
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
		void kamerAanwezig_kamerGesloten_geeftFalseTerug(@Mock Kamer kamer) {
			when(dao.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
			when(kamer.getKamerFase()).thenReturn(KamerFase.GESLOTEN);
			assertFalse(sut.kanDeelnemen(KAMER_CODE));
		}

		@Test
		void kamerAanwezig_kamerNietGesloten_geeftTrueTerug(@Mock Kamer kamer) {
			when(dao.read(Kamer.class, KAMER_CODE)).thenReturn(Optional.of(kamer));
			// Een kamer is standaard open
			assertTrue(sut.kanDeelnemen(KAMER_CODE));
		}

	}

	@Nested
	class getKaartenSets {

		@Mock
		private DeelnemerId deelnemerId;

		@Mock
		private Kamer kamer;

		@Mock
		private Deelnemer deelnemer;

		@BeforeEach
		void setUp() {
			when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
			when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(deelnemer));
		}

		@Test
		void deelnemerHeeftGeenRol_gooitDeelnemerHeeftGeenRolException() {
			// Een gemockte deelnemer heeft sowieso geen rol
			assertThrows(DeelnemerHeeftGeenRolException.class, () -> sut.getKaartenSets(deelnemerId));
		}

		@Test
		void deelnemerHeeftRol_rolIsProjectBureau_geeftAlleKaartenSetsTerug() {
			when(deelnemer.getRol()).thenReturn(StandaardRol.PROJECTBUREAU.getRol());

			var expected = new DeelnemerKaartenSets(
				kamer.getRelevanteRollen().stream()
				     .flatMap(r -> r.getKaartenSets().stream())
				     .collect(Collectors.toSet()),
				false
			);

			var actual = sut.getKaartenSets(deelnemerId);

			assertAll(
				() -> assertEquals(expected.isGekozen(), actual.isGekozen()),
				() -> assertIterableEquals(expected.getKaartenSets(), actual.getKaartenSets())
			);
		}

		@Test
		void deelnemerHeeftRol_rolIsNietProjectBureau_geeftRelevanteKaartenSetsTerug(@Mock Rol rol) {
			when(deelnemer.getRol()).thenReturn(rol);
			var expected = new DeelnemerKaartenSets(deelnemer.getRol().getKaartenSets(), true);
			var actual = sut.getKaartenSets(deelnemerId);

			assertAll(
				() -> assertEquals(expected.isGekozen(), actual.isGekozen()),
				() -> assertIterableEquals(expected.getKaartenSets(), actual.getKaartenSets())
			);
		}

	}

	@Nested
	class highlightKaart {

		@Mock
		private DeelnemerId deelnemerId;

		@Mock
		private KaartToelichting kaartToelichting;

		@Mock
		private Kamer kamer;

		@BeforeEach
		void setUp() {
			when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
		}

		@Test
		void rondeAfwezig_gooitRondeNietGevondenException() {
			when(kamer.getHuidigeRonde()).thenReturn(Optional.empty());
			assertThrows(RondeNietGevondenException.class, () -> sut.highlightKaart(deelnemerId, kaartToelichting));
		}

		@Test
		void rondeAanwezig_kanHighlighten_zetGehighlighteKaart(@Mock Ronde ronde) {
			when(kamer.getHuidigeRonde()).thenReturn(Optional.of(ronde));
			when(ronde.getGehighlighteKaart()).thenReturn(Optional.empty());

			sut.highlightKaart(deelnemerId, kaartToelichting);

			verify(ronde).setGehighlighteKaart(kaartToelichting);
		}

		@Test
		void rondeAanwezig_kanNietHighlighten_gooitVerbodenToegangException(
			@Mock Ronde ronde,
			@Mock KaartToelichting gehighlighteKaart
		) {
			when(kamer.getHuidigeRonde()).thenReturn(Optional.of(ronde));
			when(ronde.getGehighlighteKaart()).thenReturn(Optional.of(gehighlighteKaart));
			assertThrows(VerbodenToegangException.class, () -> sut.highlightKaart(deelnemerId, kaartToelichting));
		}

	}

	@Nested
	class highlightVolgendeKaart {

		@Mock
		private DeelnemerId deelnemerId;

		@Mock
		private Kamer kamer;

		@BeforeEach
		void setUp() {
			when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
		}

		@Test
		void deelnemerIsBegeleider_kanVolgendeKaartNietHighlighten_gooitRondeNietGevondenException(
			@Mock Begeleider begeleider, @Mock Ronde ronde
		) {
			when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(begeleider));
			when(kamer.getHuidigeRonde()).thenReturn(Optional.empty());
			assertThrows(RondeNietGevondenException.class, () -> sut.highlightVolgendeKaart(deelnemerId));
		}

		@Test
		void deelnemerIsBegeleider_kanVolgendeKaartHighlighten_setGehighlighteKaartOpNull(
			@Mock Begeleider begeleider,
			@Mock Ronde ronde
		) {
			when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(begeleider));
			when(kamer.getHuidigeRonde()).thenReturn(Optional.of(ronde));
			sut.highlightVolgendeKaart(deelnemerId);
			verify(ronde).setGehighlighteKaart(null);
		}

		@Test
		void deelnemerIsGeenBegeleider_gooitVerbodenToegangException(@Mock Deelnemer deelnemer) {
			when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(deelnemer));
			assertThrows(VerbodenToegangException.class, () -> sut.highlightVolgendeKaart(deelnemerId));
		}

	}

	@Nested
	class kaartNaarSelectie {

		@Mock
		private DeelnemerId deelnemerId;

		@Mock
		private Kaart kaart;

		@Mock
		private Deelnemer deelnemer;

		@BeforeEach
		void setUp(@Mock Kamer kamer) {
			when(dao.read(Kamer.class, deelnemerId.getKamerCode())).thenReturn(Optional.of(kamer));
			when(kamer.getDeelnemer(deelnemerId)).thenReturn(Optional.of(deelnemer));
		}

		@Test
		void kaartenSelectieNietAanwezig_maaktEnZetKaartenSelectie() {
			sut.kaartNaarSelectie(deelnemerId, kaart);
			verify(deelnemer).setKaartenSelectie(notNull());
		}

		@Nested
		class kaartenSelectieAanwezig {

			@Mock
			private KaartenSelectie kaartenSelectie;

			@BeforeEach
			void setUp() {
				when(deelnemer.getKaartenSelectie()).thenReturn(kaartenSelectie);
			}

			@Test
			void kaartIsGeselecteerd_verwijdertKaartEnGeeftFalseTerug() {
				when(kaartenSelectie.kaartIsGeselecteerd(kaart)).thenReturn(true);
				assertFalse(sut.kaartNaarSelectie(deelnemerId, kaart));
				verify(kaartenSelectie).kaartIsGeselecteerd(kaart);
				verify(kaartenSelectie).removeKaart(kaart);
			}

			@Test
			void kaartIsNietGeselecteerd_voegtKaartToeEnGeeftTrueTerug() {
				assertTrue(sut.kaartNaarSelectie(deelnemerId, kaart));
				verify(kaartenSelectie).addKaart(kaart);
			}

			@Test
			void kaartenSelectieIsVol_gooitKaartenSelectieVolException() {
				when(kaartenSelectie.isVol()).thenReturn(true);
				assertThrows(KaartenSelectieVolException.class, () -> sut.kaartNaarSelectie(deelnemerId, kaart));
			}

		}

	}

}
