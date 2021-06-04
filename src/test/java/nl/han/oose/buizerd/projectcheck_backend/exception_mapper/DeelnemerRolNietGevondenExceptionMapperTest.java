package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.exception.DeelnemerRolNietGevondenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeelnemerRolNietGevondenExceptionMapperTest {

	private DeelnemerRolNietGevondenExceptionMapper mapper;

	@BeforeEach
	void setup() {
		mapper = new DeelnemerRolNietGevondenExceptionMapper();
	}

	@Test
	void geeftJuisteResponse(@Mock DeelnemerRolNietGevondenException exception, @Mock Response.ResponseBuilder responseBuilder) {
		try (MockedStatic<Response> mock = Mockito.mockStatic(Response.class)) {
			mock.when(() -> Response.status(Response.Status.NO_CONTENT)).thenReturn(responseBuilder);
			Assertions.assertEquals(responseBuilder.build(), mapper.toResponse(exception));
		}
	}

}
