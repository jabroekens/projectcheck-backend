package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class KaartenSelectie {

	private static int aantalKaartenSelecties = 0;

	static final int MAX_KAARTEN = 3;

	@Id
	@Column(nullable = false, updatable = false)
	private int selectieCode;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<Kaart> kaarten = new HashSet<>();

	/**
	 * @deprecated wordt gebruikt door JPA en mag niet aangeroepen worden
	 */
	@ExcludeFromGeneratedCoverageReport(reason = "wordt gebruikt door JPA en mag niet aangeroepen worden")
	@Deprecated
	protected KaartenSelectie() {
	}

	public KaartenSelectie(@NotNull @Valid Set<Kaart> kaarten) {
		this.kaarten = kaarten;
		this.selectieCode = aantalKaartenSelecties++;
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
