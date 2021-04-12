package nl.han.oose.buizerd.projectcheck_backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PersonTest {

	@Test
	void shouldGiveNullPointerException() {
		Assertions.assertThrows(NullPointerException.class, () -> new Person(null));
	}

	@ParameterizedTest
	@ValueSource(strings = {"John", "Jane"})
	void shouldGiveName(String name) {
		var person = new Person(name);
		Assertions.assertEquals(name, person.getName());
	}

	@ParameterizedTest
	@ValueSource(strings = {"John", "Jane"})
	void shouldSetName(String name) {
		var person = new Person(name);
		person.setName(name + "2");
		Assertions.assertEquals(name + "2", person.getName());
	}

}