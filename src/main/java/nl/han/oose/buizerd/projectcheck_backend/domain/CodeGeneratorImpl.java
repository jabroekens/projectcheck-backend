package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Locale;

// Zie: https://stackoverflow.com/a/41156
public class CodeGeneratorImpl implements CodeGenerator {

	private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVW";
	private static final String LOWER = UPPER.toLowerCase(Locale.ROOT);
	private static final String DIGITS = "0123456789";
	static final String ALPHANUM = UPPER + LOWER + DIGITS;

	private final SecureRandom secureRandom;

	public CodeGeneratorImpl() throws NoSuchAlgorithmException {
		secureRandom = SecureRandom.getInstance("SHA1PRNG");
	}

	@Override
	public String genereerCode(int maxLengte) {
		var code = new StringBuilder();
		var karakters = ALPHANUM.toCharArray();

		for (var i = 0; i < maxLengte; i++) {
			code.append(karakters[secureRandom.nextInt(maxLengte)]);
		}

		return code.toString();
	}

}
