package nl.han.oose.buizerd.projectcheck_backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletContextEvent;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
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
		void rollenBestaanNiet_slaatRollenOp() {
			when(dao.read(any(), anyString())).thenReturn(Optional.empty());

			sut.contextInitialized(servletContextEvent);

			var times = times(StandaardRol.values().length);
			verify(dao, times).read(any(), anyString());
			verify(dao, times).create(any(Rol.class));
		}

		@Test
		void rollenBestaan_slaatRollenNietOp(@Mock Rol rol) {
			when(dao.read(any(), anyString())).thenReturn(Optional.of(rol));

			sut.contextInitialized(servletContextEvent);

			verify(dao, times(StandaardRol.values().length)).read(any(), anyString());
			verifyNoMoreInteractions(dao);
		}

	}

}
