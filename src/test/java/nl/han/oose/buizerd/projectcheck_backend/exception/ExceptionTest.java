package nl.han.oose.buizerd.projectcheck_backend.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

abstract class ExceptionTest<T extends Throwable> {

	private final String message;

	private T t;

	protected ExceptionTest(String message) {
		this.message = message;
	}

	@BeforeEach
	void setUp() {
		t = setUpImpl();
	}

	protected abstract T setUpImpl();

	@Test
	void geeftJuisteMessage() {
		Assertions.assertEquals(
			message,
			t.getMessage()
		);
	}

}
