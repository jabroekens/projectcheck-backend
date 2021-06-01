package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerGeslotenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerGeslotenExceptionMapperTest {

	private KamerGeslotenExceptionMapper sut;

	@BeforeEach
	void setUp() {
		sut = new KamerGeslotenExceptionMapper();
	}

	@Test
	void toResponse_geeftJuisteWaarde(@Mock KamerGeslotenException exception, @Mock Response.ResponseBuilder responseBuilder) {
		try (MockedStatic<Response> mock = mockStatic(Response.class)) {
			mock.when(() -> Response.status(Response.Status.FORBIDDEN)).thenReturn(responseBuilder);
			assertEquals(responseBuilder.build(), sut.toResponse(exception));
		}
	}

}
