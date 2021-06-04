package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerRolNietGevondenException;

@Provider
public class DeelnemerRolNietGevondenExceptionMapper implements ExceptionMapper<DeelnemerRolNietGevondenException> {

	@Override
	public Response toResponse(DeelnemerRolNietGevondenException exception) {
		return Response.status(Response.Status.NO_CONTENT).build();
	}

}
