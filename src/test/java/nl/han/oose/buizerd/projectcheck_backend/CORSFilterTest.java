package nl.han.oose.buizerd.projectcheck_backend;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CORSFilterTest {

	private CORSFilter filter;

	@BeforeEach
	void setUp() {
		filter = new CORSFilter();
	}

	@Test
	void voegtJuisteHeadersToe(
		@Mock ContainerRequestContext requestContext,
		@Mock ContainerResponseContext responseContext,
		@Mock MultivaluedMap<String, Object> headers
	) {
		Mockito.when(responseContext.getHeaders()).thenReturn(headers);
		filter.filter(requestContext, responseContext);
		Mockito.verify(responseContext.getHeaders()).add("Access-Control-Allow-Origin", "*");
		Mockito.verify(responseContext.getHeaders()).add("Access-Control-Allow-Methods", "GET, POST, HEAD");
	}

}
