package nl.han.oose.buizerd.projectcheck_backend;

import jakarta.servlet.ServletContextEvent;
import java.util.Optional;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.verification.VerificationMode;

@ExtendWith(MockitoExtension.class)
public class StartupListenerTest {

	@Mock
	private DAO<Rol, String> rolDAO;

	private StartupListener startupListener;

	@BeforeEach
	void setUp() {
		this.startupListener = new StartupListener(rolDAO);
	}

	@Test
	void contextInitialized_rollenBestaanNiet_slaatRollenOp(@Mock ServletContextEvent sce) {
		Mockito.when(rolDAO.read(Mockito.any(), Mockito.anyString())).thenReturn(Optional.empty());

		startupListener.contextInitialized(sce);

		VerificationMode times = Mockito.times(StandaardRol.values().length);
		Mockito.verify(rolDAO, times).read(Mockito.any(), Mockito.anyString());
		Mockito.verify(rolDAO, times).create(Mockito.any(Rol.class));
	}

	@Test
	void contextInitialized_rollenBestaan_slaatRollenNietOp(@Mock ServletContextEvent sce, @Mock Rol rol) {
		Mockito.when(rolDAO.read(Mockito.any(), Mockito.anyString())).thenReturn(Optional.of(rol));

		startupListener.contextInitialized(sce);

		Mockito.verify(rolDAO, Mockito.times(StandaardRol.values().length)).read(Mockito.any(), Mockito.anyString());
		Mockito.verifyNoMoreInteractions(rolDAO);
	}

}