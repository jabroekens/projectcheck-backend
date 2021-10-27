package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.HashSet;
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
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;
import nl.han.oose.buizerd.projectcheck_backend.dto.DeelnemerKaartenSets;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerHeeftGeenRolException;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KaartenSelectieVolException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.RondeNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;

@Transactional
public class KamerServiceImpl implements KamerService {

	private DAO dao;

	@Override
	public boolean kanDeelnemen(String kamerCode) {
		var kamer = dao.read(Kamer.class, kamerCode);
		return kamer.isPresent() && kamer.get().getKamerFase() != KamerFase.GESLOTEN;
	}

	@Override
	public Set<Rol> getRelevanteRollen(String kamerCode) throws KamerNietGevondenException {
		return getKamer(kamerCode).getRelevanteRollen();
	}

	@Override
	public Set<Rol> getStandaardRollen() {
		return Arrays.stream(StandaardRol.values())
		             .map(StandaardRol::getRol)
		             .collect(Collectors.toSet());
	}

	@Override
	public DeelnemerKaartenSets getKaartenSets(DeelnemerId deelnemerId)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, DeelnemerHeeftGeenRolException {
		var kamer = getKamer(deelnemerId.getKamerCode());
		var deelnemer = getDeelnemer(kamer, deelnemerId);

		if (deelnemer.getRol() == null) {
			throw new DeelnemerHeeftGeenRolException(deelnemerId.getId(), deelnemerId.getKamerCode());
		}

		if (deelnemer.getRol().equals(StandaardRol.PROJECTBUREAU.getRol())) {
			return new DeelnemerKaartenSets(
				kamer.getRelevanteRollen().stream()
				     .flatMap(r -> r.getKaartenSets().stream())
				     .collect(Collectors.toSet()),
				false
			);
		} else {
			return new DeelnemerKaartenSets(deelnemer.getRol().getKaartenSets(), true);
		}
	}

	@Override
	public void highlightKaart(DeelnemerId deelnemerId, KaartToelichting kaartToelichting)
	throws KamerNietGevondenException, VerbodenToegangException, RondeNietGevondenException {
		var kamer = getKamer(deelnemerId.getKamerCode());
		var huidigeRonde = kamer.getHuidigeRonde();

		if (huidigeRonde.isPresent()) {
			if (huidigeRonde.get().getGehighlighteKaart().isEmpty()) {
				huidigeRonde.get().setGehighlighteKaart(kaartToelichting);
			} else {
				throw new VerbodenToegangException(deelnemerId.getId(), deelnemerId.getKamerCode());
			}
		} else {
			throw new RondeNietGevondenException(kamer.getKamerCode());
		}
	}

	@Override
	public void highlightVolgendeKaart(DeelnemerId deelnemerId)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, RondeNietGevondenException,
	       VerbodenToegangException {
		var kamer = getKamer(deelnemerId.getKamerCode());
		var deelnemer = getDeelnemer(kamer, deelnemerId);

		if (deelnemer instanceof Begeleider) {
			var huidigeRonde = kamer.getHuidigeRonde();

			if (huidigeRonde.isPresent()) {
				huidigeRonde.get().setGehighlighteKaart(null);
			} else {
				throw new RondeNietGevondenException(kamer.getKamerCode());
			}
		} else {
			throw new VerbodenToegangException(deelnemerId.getId(), deelnemerId.getKamerCode());
		}
	}

	@Override
	public boolean kaartNaarSelectie(DeelnemerId deelnemerId, Kaart kaart)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, KaartenSelectieVolException {
		var kamer = getKamer(deelnemerId.getKamerCode());
		var deelnemer = getDeelnemer(kamer, deelnemerId);
		var kaartenSelectie = deelnemer.getKaartenSelectie();

		if (kaartenSelectie == null) {
			kaartenSelectie = new KaartenSelectie(new HashSet<>());
			deelnemer.setKaartenSelectie(kaartenSelectie);
		}

		if (kaartenSelectie.kaartIsGeselecteerd(kaart)) {
			kaartenSelectie.removeKaart(kaart);
			return false;
		} else if (!kaartenSelectie.isVol()) {
			kaartenSelectie.addKaart(kaart);
			return true;
		} else {
			throw new KaartenSelectieVolException(deelnemerId.getId(), deelnemerId.getKamerCode());
		}
	}

	@Override
	public void kiesRelevanteRollen(DeelnemerId deelnemerId, Set<Rol> relevanteRollen)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, VerbodenToegangException {
		var kamer = getKamer(deelnemerId.getKamerCode());
		var deelnemer = getDeelnemer(kamer, deelnemerId);

		if (deelnemer instanceof Begeleider) {
			kamer.setRelevanteRollen(relevanteRollen);
		} else {
			throw new VerbodenToegangException(deelnemerId.getId(), deelnemerId.getKamerCode());
		}

	}

	@Override
	public void kiesRol(DeelnemerId deelnemerId, Rol rol)
	throws KamerNietGevondenException, DeelnemerNietGevondenException {
		var kamer = getKamer(deelnemerId.getKamerCode());
		getDeelnemer(kamer, deelnemerId).setRol(rol);
	}

	@Override
	public KamerFase naarVolgendeFase(DeelnemerId deelnemerId)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, VerbodenToegangException {
		var kamer = getKamer(deelnemerId.getKamerCode());
		var deelnemer = getDeelnemer(kamer, deelnemerId);

		if (deelnemer instanceof Begeleider) {
			var kamerFase = kamer.getKamerFase();
			var volgendeFase = kamerFase.getVolgendeFase();

			kamer.setKamerFase(volgendeFase);
			return volgendeFase;
		}

		throw new VerbodenToegangException(deelnemerId.getId(), deelnemerId.getKamerCode());
	}

	private Kamer getKamer(String kamerCode) {
		var kamer = dao.read(Kamer.class, kamerCode);
		return kamer.orElseThrow(() -> new KamerNietGevondenException(kamerCode));
	}

	private Deelnemer getDeelnemer(Kamer kamer, DeelnemerId deelnemerId) {
		var deelnemer = kamer.getDeelnemer(deelnemerId);
		return deelnemer.orElseThrow(() -> new DeelnemerNietGevondenException(deelnemerId.getId(), deelnemerId.getKamerCode()));
	}

	@Inject
	public void setDao(DAO dao) {
		this.dao = dao;
	}

}
