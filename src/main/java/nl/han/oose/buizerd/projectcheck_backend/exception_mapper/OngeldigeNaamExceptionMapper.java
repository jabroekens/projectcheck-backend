package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import nl.han.oose.buizerd.projectcheck_backend.exception.OngeldigeNaamException;

public class OngeldigeNaamExceptionMapper implements ExceptionMapper<OngeldigeNaamException> {

	@Override
	public Response toResponse(OngeldigeNaamException e) {
		return Response.status(Response.Status.FORBIDDEN).build();
	}

}
