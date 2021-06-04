package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.websocket.Session;
import java.util.HashSet;
import java.util.Set;
import nl.han.oose.buizerd.projectcheck_backend.domain.Deelnemer;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSet;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import nl.han.oose.buizerd.projectcheck_backend.domain.Rol;
import nl.han.oose.buizerd.projectcheck_backend.domain.StandaardRol;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HaalKaartenSetOpEventTest {

	private HaalKaartenSetOpEvent haalKaartenSetOpEvent;

	@BeforeEach
	void setup() {
		haalKaartenSetOpEvent = new HaalKaartenSetOpEvent();
	}

	@Nested
	class voerUit {

		@Test
		void deelnemerKrijgtDeRelevanteKaartenSetsVoorExternProjectTeamlid(
			@Mock Deelnemer deelnemer, @Mock Session session,
			@Mock Rol rol, @Mock KaartenSet kaartenSet
		) {
			// Assign.
			Set<KaartenSet> expectedContextKaartenSets = new HashSet<>();
			expectedContextKaartenSets.add(kaartenSet);

			Mockito.when(deelnemer.getRol()).thenReturn(rol);
			Mockito.when(rol.getKaartenSets()).thenReturn(expectedContextKaartenSets);

			// Act.
			EventResponse response = haalKaartenSetOpEvent.voerUit(deelnemer, session);

			// Assert.
			Assertions.assertAll(
				() -> Assertions.assertTrue(response.getContext().get("kaartensets") instanceof Iterable<?>),
				() -> Assertions.assertIterableEquals(expectedContextKaartenSets, (Iterable<?>) response.getContext().get("kaartensets")),
				() -> Assertions.assertTrue(response.getContext().get("gekozen") instanceof Boolean),
				() -> Assertions.assertTrue((Boolean) response.getContext().get("gekozen")),
				() -> Assertions.assertEquals(EventResponse.Status.OK, response.getStatus())
			);
		}

		@Test
		void deelnemerKrijgtDeRelevanteKaartenSetsVoorProjectBureau(
			@Mock Deelnemer deelnemer, @Mock Session session,
			@Mock Rol rol, @Mock Kamer kamer
		) {
			// Assign.
			Set<KaartenSet> expectedContextKaartenSets = new HashSet<>();

			Mockito.when(deelnemer.getKamer()).thenReturn(kamer);
			Mockito.when(kamer.getRelevanteRollen()).thenReturn(Set.of(rol));
			Mockito.when(deelnemer.getRol()).thenReturn(StandaardRol.PROJECTBUREAU.getRol());
			Mockito.when(rol.getKaartenSets()).thenReturn(expectedContextKaartenSets);

			// Act.
			EventResponse response = haalKaartenSetOpEvent.voerUit(deelnemer, session);

			// Assert.
			Assertions.assertAll(
				() -> Assertions.assertTrue(response.getContext().get("kaartensets") instanceof Iterable<?>),
				() -> Assertions.assertIterableEquals(expectedContextKaartenSets, (Iterable<?>) response.getContext().get("kaartensets")),
				() -> Assertions.assertTrue(response.getContext().get("gekozen") instanceof Boolean),
				() -> Assertions.assertFalse((Boolean) response.getContext().get("gekozen")),
				() -> Assertions.assertEquals(EventResponse.Status.OK, response.getStatus())
			);
		}

	}

}
