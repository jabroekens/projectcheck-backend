package nl.han.oose.buizerd.projectcheck_backend;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import nl.han.oose.buizerd.projectcheck_backend.dao.DAO;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardKaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;

@WebListener
public class StartupListener implements ServletContextListener {

	private DAO dao;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		for (StandaardRol standaardRol : StandaardRol.values()) {
			if (dao.read(Rol.class, standaardRol.getRol().getRolNaam()).isEmpty()) {
				dao.create(standaardRol.getRol());
			}
		}

		for (StandaardKaartenSet standaardKaartenSet : StandaardKaartenSet.values()) {
			KaartenSet kaartenSet = standaardKaartenSet.getKaartenSet();
			if (dao.read(KaartenSet.class, kaartenSet.getId()).isEmpty()) {
				dao.create(kaartenSet);
			}
		}
	}

	@Inject
	void setDao(DAO dao) {
		this.dao = dao;
	}

}
