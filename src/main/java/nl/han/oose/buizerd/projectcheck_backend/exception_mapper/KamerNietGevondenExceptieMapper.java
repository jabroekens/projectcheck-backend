package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.buizerd.projectcheck_backend.exceptions.KamerNietGevondenExceptie;

@Provider
public class KamerNietGevondenExceptieMapper implements ExceptionMapper<KamerNietGevondenExceptie> {

	@Override
	public Response toResponse(KamerNietGevondenExceptie kamerNietGevondenExceptie) {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

}
