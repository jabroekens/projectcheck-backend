package nl.han.oose.buizerd.projectcheck_backend;

import java.util.Objects;

public class Person {

	private String name;

	public Person(String name) {
		this.name = Objects.requireNonNull(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}