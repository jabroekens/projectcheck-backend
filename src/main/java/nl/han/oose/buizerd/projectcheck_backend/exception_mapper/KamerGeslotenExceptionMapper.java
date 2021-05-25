package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;

@Provider
public class KamerGeslotenExceptionMapper implements ExceptionMapper<KamerGeslotenException> {

	@Override
	public Response toResponse(KamerGeslotenException kamerGeslotenException) {
		return Response.status(Response.Status.FORBIDDEN).build();
	}

}
