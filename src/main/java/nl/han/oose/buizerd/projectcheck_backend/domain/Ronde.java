package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Transient;
import java.util.Optional;

public class Ronde {

	@Transient
	private KaartToelichting gehighlighteKaart;


	public void setGehighlighteKaart(KaartToelichting kaartOmTeHighlighten) {
		this.gehighlighteKaart = kaartOmTeHighlighten;

	}

	public Optional<KaartToelichting> getGehighlighteKaart() {
		return Optional.ofNullable(this.gehighlighteKaart);

	}

}
