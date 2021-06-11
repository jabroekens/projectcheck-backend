package nl.han.oose.buizerd.projectcheck_backend.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class ExceptionTest<T extends Throwable> {

	private final String message;

	private T sut;

	protected ExceptionTest(String message) {
		this.message = message;
	}

	@BeforeEach
	void setUp() {
		sut = setUpImpl();
	}

	protected abstract T setUpImpl();

	@Test
	void getMessage_geeftJuisteWaarde() {
		assertEquals(message, sut.getMessage());
	}

}
