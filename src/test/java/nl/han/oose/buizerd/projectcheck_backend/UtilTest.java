package nl.han.oose.buizerd.projectcheck_backend;

import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {

	@Test
	void verbiedInstantiatie() {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			/*
			 * Het is mogelijk dat andere excepties worden gegooid (zie Javadoc van java.lang.reflect),
			 * maar we willen testen dat, in normale omstandigheden, een UnsupportedOperationException
			 * wordt gegooid bij instantiëren van Util.
			 *
			 * In andere woorden: `new Util()` moet altijd een exceptie gooien.
			 */
			try {
				Constructor<Util> constructor = Util.class.getDeclaredConstructor();
				constructor.setAccessible(true);
				constructor.newInstance();
			} catch (Exception e) {
				throw new UnsupportedOperationException();
			}
		});
	}

	@Test
	void geeftDezelfdeGsonInstantie() {
		Assertions.assertSame(Util.getGson(), Util.getGson());
	}

}
