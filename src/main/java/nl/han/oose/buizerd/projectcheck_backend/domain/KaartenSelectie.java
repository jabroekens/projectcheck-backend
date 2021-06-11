package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class KaartenSelectie {

	static final int MAX_KAARTEN = 3;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	private int selectieCode;

	@OneToMany
	private Set<Kaart> kaarten = new HashSet<>();

	/**
	 * Construeert een {@link KaartenSelectie}.
	 * <p>
	 * <b>Deze constructor wordt gebruikt door JPA en mag niet aangeroepen worden.</b>
	 */
	public KaartenSelectie() {
	}

	public KaartenSelectie(@NotNull @Valid Set<Kaart> kaarten) {
		this.kaarten = kaarten;
	}

	public boolean kaartIsGeselecteerd(Kaart kaart) {
		return kaarten.contains(kaart);
	}

	public void addKaart(Kaart kaart) {
		if (kaarten.size() < MAX_KAARTEN) {
			this.kaarten.add(kaart);
		}
	}

	public void removeKaart(Kaart kaart) {
		this.kaarten.remove(kaart);
	}

	public boolean isVol() {
		return kaarten.size() == MAX_KAARTEN;
	}

}
