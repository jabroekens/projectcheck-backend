package nl.han.oose.buizerd.projectcheck_backend;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bevat utility methodes.
 */
public final class Util {

	private static final Gson GSON;

	static {
		/*
		 * Helaas is dit niet inbegrepen bij Gson zelf,
		 * ookal is hier wel een pull request voor.
		 *
		 * Zie: https://github.com/google/gson/pull/1262
		 */
		GSON = new GsonBuilder()
			.enableComplexMapKeySerialization()
			.addSerializationExclusionStrategy(new ExclusionStrategy() {
				@Override
				public boolean shouldSkipField(FieldAttributes fieldAttributes) {
					return fieldAttributes.getAnnotation(GsonExclude.class) != null;
				}

				@Override
				public boolean shouldSkipClass(Class<?> aClass) {
					return false;
				}
			}).create();
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

	/**
	 * Geeft aan dat een veld niet geserializeerd moet worden door {@link Gson}.
	 *
	 * @see com.google.gson.ExclusionStrategy
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface GsonExclude {

	}

}
