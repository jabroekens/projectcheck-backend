package nl.han.oose.buizerd.projectcheck_backend;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CORSFilterTest {

	private CORSFilter sut;

	@BeforeEach
	void setUp() {
		sut = new CORSFilter();
	}

	@Test
	void filter_voegtJuisteHeadersToe(
		@Mock ContainerRequestContext requestContext,
		@Mock ContainerResponseContext responseContext,
		@Mock MultivaluedMap<String, Object> headers
	) {
		when(responseContext.getHeaders()).thenReturn(headers);
		sut.filter(requestContext, responseContext);
		verify(responseContext.getHeaders()).add("Access-Control-Allow-Origin", "*");
		verify(responseContext.getHeaders()).add("Access-Control-Allow-Methods", "GET, POST, HEAD");
	}

}
