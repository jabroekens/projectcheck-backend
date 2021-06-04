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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

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

	@Test
	void contextInitialized_rollenBestaanNiet_slaatRollenOp(@Mock ServletContextEvent sce) {
		when(dao.read(any(), anyString())).thenReturn(Optional.empty());

		sut.contextInitialized(sce);

		VerificationMode times = times(StandaardRol.values().length);
		verify(dao, times).read(any(), anyString());
		verify(dao, times).create(any(Rol.class));
	}

	@Test
	void contextInitialized_rollenBestaan_slaatRollenNietOp(@Mock ServletContextEvent sce, @Mock Rol rol) {
		when(dao.read(any(), anyString())).thenReturn(Optional.of(rol));

		sut.contextInitialized(sce);

		verify(dao, times(StandaardRol.values().length)).read(any(), anyString());
		verifyNoMoreInteractions(dao);
	}

}
