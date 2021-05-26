package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KamerGeslotenExceptionMapperTest {

	private KamerGeslotenExceptionMapper mapper;

	@BeforeEach
	void setUp() {
		mapper = new KamerGeslotenExceptionMapper();
	}

	@Test
	void geeftJuisteReponse(@Mock KamerGeslotenException exception, @Mock Response.ResponseBuilder responseBuilder) {
		try (MockedStatic<Response> mock = Mockito.mockStatic(Response.class)) {
			mock.when(() -> Response.status(Response.Status.FORBIDDEN)).thenReturn(responseBuilder);
			Assertions.assertEquals(responseBuilder.build(), mapper.toResponse(exception));
		}
	}

}
