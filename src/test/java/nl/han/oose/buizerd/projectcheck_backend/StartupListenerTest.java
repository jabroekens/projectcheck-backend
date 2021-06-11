package nl.han.oose.buizerd.projectcheck_backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletContextEvent;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardKaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StartupListenerTest {

	@Mock
	private DAO dao;

	private StartupListener sut;

	@BeforeEach
	void setUp() {
		sut = new StartupListener();
		sut.setDao(dao);
	}

	@Nested
	class contextInitialized {

		@Mock
		private ServletContextEvent servletContextEvent;

		@Test
		void waardenBestaanNiet_slaatWaardenOp() {
			when(dao.read(eq(Rol.class), any())).thenReturn(Optional.empty());
			when(dao.read(eq(KaartenSet.class), any())).thenReturn(Optional.empty());

			sut.contextInitialized(servletContextEvent);

			slaatWaardenOpAlsDezeNietBestaan(StandaardRol.class, Rol.class);
			slaatWaardenOpAlsDezeNietBestaan(StandaardKaartenSet.class, KaartenSet.class);
		}

		@Test
		void waardenBestaan_slaatWaardenNietOp(@Mock Rol rol, @Mock KaartenSet kaartenSet) {
			when(dao.read(eq(Rol.class), any())).thenReturn(Optional.of(rol));
			when(dao.read(eq(KaartenSet.class), any())).thenReturn(Optional.of(kaartenSet));

			sut.contextInitialized(servletContextEvent);

			slaatWaardenNietOpAlsDezeBestaan(StandaardRol.class, Rol.class);
			slaatWaardenNietOpAlsDezeBestaan(StandaardKaartenSet.class, KaartenSet.class);
		}

		private <E extends Enum<E>, T> void slaatWaardenOpAlsDezeNietBestaan(Class<E> enumKlasse, Class<T> klasseType) {
			var times = times(enumKlasse.getEnumConstants().length);
			verify(dao, times).read(eq(klasseType), any());
			verify(dao, times).create(any(klasseType));
		}

		private <E extends Enum<E>, T> void slaatWaardenNietOpAlsDezeBestaan(Class<E> enumKlasse, Class<T> klasseType) {
			verify(dao, times(enumKlasse.getEnumConstants().length)).read(eq(klasseType), any());
			verify(dao, never()).create(any(klasseType));
		}

	}

}
