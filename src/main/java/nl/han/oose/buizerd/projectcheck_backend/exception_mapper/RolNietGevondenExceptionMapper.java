package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.buizerd.projectcheck_backend.exception.RolNietGevondenException;

@Provider
public class RolNietGevondenExceptionMapper implements ExceptionMapper<RolNietGevondenException> {

	@Override
	public Response toResponse(RolNietGevondenException rolNietGevondenException) {
		return Response.status(Response.Status.NOT_FOUND).build();
	}
}
