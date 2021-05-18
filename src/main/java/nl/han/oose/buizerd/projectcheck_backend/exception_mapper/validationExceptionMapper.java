package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.validation.ValidationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

public class validationExceptionMapper implements ExceptionMapper<ValidationException> {

	@Override
	public Response toResponse(ValidationException e) {
		return Response.status(Response.Status.FORBIDDEN).build();
	}

}
