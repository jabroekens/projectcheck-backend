package nl.han.oose.buizerd.projectcheck_backend;

import com.google.gson.Gson;

public final class Util {

	private static final Gson GSON;

	static {
		GSON = new Gson();
	}

	public static Gson getGson() {
		return Util.GSON;
	}

	private Util() {
		throw new UnsupportedOperationException("Cannot instantiate this utility class.");
	}

}
