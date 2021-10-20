package nl.han.oose.buizerd.projectcheck_backend.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.concurrent.CompletableFuture;
import nl.han.oose.buizerd.projectcheck_backend.event.Event;
import nl.han.oose.buizerd.projectcheck_backend.event.EventResponse;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import nl.han.oose.buizerd.projectcheck_backend.validation.constraints.KamerCode;

public interface KamerService {

	/**
	 * Geeft aan of er (nog) deelgenomen kan worden aan de kamer met kamercode {@code kamerCode}.
	 */
	boolean kanDeelnemen(@KamerCode String kamerCode);

	CompletableFuture<EventResponse> voerEventUit(@NotNull @Valid Event event)
	throws DeelnemerNietGevondenException, KamerNietGevondenException;

}
