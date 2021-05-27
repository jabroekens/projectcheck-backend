package nl.han.oose.buizerd.projectcheck_backend.event;

import jakarta.jms.Session;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kaart;
import nl.han.oose.buizerd.projectcheck_backend.domain.KaartenSelectie;
import nl.han.oose.buizerd.projectcheck_backend.domain.Kamer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class KaartNaarSelectieEventTest {
    KaartNaarSelectieEvent kaartNaarSelectieEvent;

    @Mock
    private Kaart kaart;

    @BeforeEach
    void setup() {
        kaartNaarSelectieEvent = new KaartNaarSelectieEvent();
        kaartNaarSelectieEvent.geselecteerdeKaart = kaart;
    }

    @Test
    void voerUit(@Mock Kamer kamer, @Mock Session session) {
        // arrange
        //Mockito.when(kamer.getDeelnemer(kaartNaarSelectieEvent.getDeelnemerId())).thenReturn(Optional.of().);
        // act

        //assert
    }
}