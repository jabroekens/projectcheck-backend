package nl.han.oose.buizerd.projectcheck_backend.domain;

/**
 * Genereert een kamercode voor een {@link Kamer}.
 */
public interface CodeGenerator {

	String genereerCode(int maxLengte);

}
