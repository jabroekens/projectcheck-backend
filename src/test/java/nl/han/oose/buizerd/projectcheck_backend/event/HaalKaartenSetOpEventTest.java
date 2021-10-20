package nl.han.oose.buizerd.projectcheck_backend.event;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HaalKaartenSetOpEventTest {

	private HaalKaartenSetOpEvent sut;

	@BeforeEach
	void setUp() {
		sut = new HaalKaartenSetOpEvent();
	}

	@Nested
	class voerUit {

		@Mock
		private Deelnemer deelnemer;

		@Test
		void deelnemerHeeftGeenRol_geeftJuisteEventResponseTerug() {
			var eventResponse = sut.voerUit(deelnemer);
			assertEquals(EventResponse.Status.ROL_NIET_GEVONDEN, eventResponse.getStatus());
		}

		@Test
		void deelnemerHeeftRolAndersDanProjectBureau_geeftJuisteRollenTerug(@Mock Rol rol, @Mock KaartenSet kaartenSet) {
			var expectedKaartenSets = Set.of(kaartenSet);
			when(deelnemer.getRol()).thenReturn(rol);
			when(rol.getKaartenSets()).thenReturn(expectedKaartenSets);

			var eventResponse = sut.voerUit(deelnemer);

			assertAll(
				() -> assertTrue(eventResponse.getContext().get("kaartensets") instanceof Iterable<?>),
				() -> assertIterableEquals(expectedKaartenSets, (Iterable<?>) eventResponse.getContext().get("kaartensets")),
				() -> assertTrue(eventResponse.getContext().get("gekozen") instanceof Boolean),
				() -> assertTrue((Boolean) eventResponse.getContext().get("gekozen")),
				() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
			);
		}

		@Test
		void deelnemerHeeftRolProjectBurea_geeftJuisteRollenTerug(
			@Mock Deelnemer deelnemer,
			@Mock Rol rol,
			@Mock Kamer kamer
		) {
			var expectedKaartenSets = Set.<KaartenSet>of();
			when(deelnemer.getKamer()).thenReturn(kamer);
			when(kamer.getRelevanteRollen()).thenReturn(Set.of(rol));
			when(deelnemer.getRol()).thenReturn(StandaardRol.PROJECTBUREAU.getRol());
			when(rol.getKaartenSets()).thenReturn(expectedKaartenSets);

			var eventResponse = sut.voerUit(deelnemer);

			assertAll(
				() -> assertTrue(eventResponse.getContext().get("kaartensets") instanceof Iterable<?>),
				() -> assertIterableEquals(expectedKaartenSets, (Iterable<?>) eventResponse.getContext().get("kaartensets")),
				() -> assertTrue(eventResponse.getContext().get("gekozen") instanceof Boolean),
				() -> Assertions.assertFalse((Boolean) eventResponse.getContext().get("gekozen")),
				() -> assertEquals(EventResponse.Status.OK, eventResponse.getStatus())
			);
		}

	}

}
