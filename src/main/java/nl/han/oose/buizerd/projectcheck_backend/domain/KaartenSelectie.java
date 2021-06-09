package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class KaartenSelectie {

    private static final int MAX_KAARTEN = 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int selectieCode;

    @OneToMany
    @Column(nullable = true)
    private Set<Kaart> kaarten = new HashSet<>();

    public KaartenSelectie() {

    }

    public KaartenSelectie(Set<Kaart> kaarten) {
        this.kaarten = kaarten;
    }

    public void removeKaart(Kaart kaart) {
        this.kaarten.remove(kaart);
    }

    public void addKaart(Kaart kaart) {
        if (kaarten.size() <= MAX_KAARTEN)
        this.kaarten.add(kaart);
    }

    public boolean kaartExists(Kaart kaart) {
        if(!kaarten.isEmpty()) {
            return kaarten.stream().anyMatch(k -> k.getCode() == kaart.getCode());
        }
        return false;
    }

    public int getLength() {
        return kaarten.size();
    }
}
