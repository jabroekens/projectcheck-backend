package nl.han.oose.buizerd.projectcheck_backend;

import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class UtilTest {

	@Test
	void geeftDezelfdeGsonInstantie() {
		try (MockedStatic<Util> util = Mockito.mockStatic(Util.class)) {
			Gson gson = Mockito.mock(Gson.class);
			util.when(Util::getGson).thenReturn(gson);
			Assertions.assertEquals(gson, Util.getGson());
		}
	}

}
