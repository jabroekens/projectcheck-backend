package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;

@Provider
public class KamerNietGevondenExceptionMapper implements ExceptionMapper<KamerNietGevondenException> {

	@Override
	public Response toResponse(KamerNietGevondenException kamerNietGevondenException) {
		return Response.status(Response.Status.NOT_FOUND).build();
	}

}
