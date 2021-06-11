package nl.han.oose.buizerd.projectcheck_backend.exception_mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

import jakarta.ws.rs.core.Response;
import nl.han.oose.buizerd.projectcheck_backend.exception.KamerNietGevondenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KamerNietGevondenExceptionMapperTest {

	private KamerNietGevondenExceptionMapper sut;

	@BeforeEach
	void setUp() {
		sut = new KamerNietGevondenExceptionMapper();
	}

	@Test
	void toResponse_geeftJuisteWaarde(@Mock KamerNietGevondenException exception, @Mock Response.ResponseBuilder responseBuilder) {
		try (MockedStatic<Response> mock = mockStatic(Response.class)) {
			mock.when(() -> Response.status(Response.Status.NOT_FOUND)).thenReturn(responseBuilder);
			assertEquals(responseBuilder.build(), sut.toResponse(exception));
		}
	}

}
