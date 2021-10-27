package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Begeleider;
import nl.han.oose.buizerd.projectcheck_backend.domain.DeelnemerId;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartToelichting;
import nl.han.oose.buizerd.projectcheck_backend.domain.KamerFase;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.dto.DeelnemerKaartenSets;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerHeeftGeenRolException;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KaartenSelectieVolException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.RondeNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.VerbodenToegangException;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;

public interface KamerService {

	/**
	 * Geeft aan of er (nog) deelgenomen kan worden aan de kamer met kamercode {@code kamerCode}.
	 */
	boolean kanDeelnemen(@KamerCode String kamerCode);

	Set<Rol> getRelevanteRollen(@KamerCode String kamerCode) throws KamerNietGevondenException;

	Set<Rol> getStandaardRollen();

	DeelnemerKaartenSets getKaartenSets(@NotNull @Valid DeelnemerId deelnemerId)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, DeelnemerHeeftGeenRolException;

	/**
	 * @throws VerbodenToegangException als er momenteel geen kaart gehighlight kan worden
	 */
	void highlightKaart(@NotNull @Valid DeelnemerId deelnemerId, @NotNull @Valid KaartToelichting kaartToelichting)
	throws KamerNietGevondenException, VerbodenToegangException, RondeNietGevondenException;

	/**
	 * @throws VerbodenToegangException als de deelnemer geen {@link Begeleider} is
	 */
	void highlightVolgendeKaart(@NotNull @Valid DeelnemerId deelnemerId)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, RondeNietGevondenException,
	       VerbodenToegangException;

	/**
	 * Voegt {@code kaart} toe aan de deelnemer's kaartenselectie als deze nog niet is toegevoegd, anders wordt deze
	 * verwijdert van de deelnemer's kaartenselectie.
	 *
	 * @return {@code true} als er een kaart is toegevoegd, anders {@code false}
	 */
	boolean kaartNaarSelectie(@NotNull @Valid DeelnemerId deelnemerId, @NotNull @Valid Kaart kaart)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, KaartenSelectieVolException;

	/**
	 * @throws VerbodenToegangException als de deelnemer geen {@link Begeleider} is
	 */
	void kiesRelevanteRollen(@NotNull @Valid DeelnemerId deelnemerId, @NotNull Set<@NotNull @Valid Rol> relevanteRollen)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, VerbodenToegangException;

	void kiesRol(@NotNull @Valid DeelnemerId deelnemerId, @NotNull @Valid Rol rol)
	throws KamerNietGevondenException, DeelnemerNietGevondenException;

	/**
	 * @throws VerbodenToegangException als de deelnemer geen {@link Begeleider} is
	 */
	KamerFase naarVolgendeFase(@NotNull @Valid DeelnemerId deelnemerId)
	throws KamerNietGevondenException, DeelnemerNietGevondenException, VerbodenToegangException;

}
