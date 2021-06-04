package nl.han.oose.buizerd.projectcheck_backend.domain;

import jakarta.persistence.Transient;

public  class Ronde {
	@Transient
	KaartToelichting gehighlighteKaart;


	public void setGehighlighteKaart(KaartToelichting kaartOmTeHighlighten){
		this.gehighlighteKaart = kaartOmTeHighlighten;


	}
	public KaartToelichting getGehighlighteKaart(){
		return this.gehighlighteKaart;

	}





}
