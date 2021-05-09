package nl.han.oose.buizerd.projectcheck_backend;

import com.google.gson.Gson;

/**
 * Bevat utility methodes.
 */
public final class Util {

	private static final Gson GSON;

	static {
		GSON = new Gson();
	}

	/**
	 * Geeft de instantie van {@link Gson}.
	 *
	 * @return De {@link Gson}-instantie.
	 */
	public static Gson getGson() {
		return Util.GSON;
	}

	private Util() {
		throw new UnsupportedOperationException("Cannot instantiate this utility class");
	}

}
