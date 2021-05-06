package nl.han.oose.buizerd.projectcheck_backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTest {

	@Test
	void geeftDezelfdeGsonInstantie() {
		Assertions.assertSame(Util.getGson(), Util.getGson());
	}

}
