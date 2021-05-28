package nl.han.oose.buizerd.projectcheck_backend;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;

@WebListener
public class StartupListener implements ServletContextListener {

	@Inject
	private DAO<Rol, String> rolDAO;

	/**
	 * Construeert een {@link StartupListener}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door Jakarta EE en mag niet aangeroepen worden.</b>
	 */
	public StartupListener() {
	}

	/**
	 * Construeert een {@link StartupListener}.
	 *
	 * <b>Deze constructor mag alleen aangeroepen worden binnen tests.</b>
	 *
	 * @param rolDAO Een {@link DAO} voor het domein {@link Rol}.
	 */
	StartupListener(DAO<Rol, String> rolDAO) {
		this.rolDAO = rolDAO;
	}

	public void contextInitialized(ServletContextEvent sce) {
		for (StandaardRol standaardRol : StandaardRol.values()) {
			if (rolDAO.read(Rol.class, standaardRol.getRol().getRolNaam()).isEmpty()) {
				rolDAO.create(standaardRol.getRol());
			}
		}
	}

}
