package nl.han.oose.buizerd.projectcheck_backend.domain;

import java.util.Set;

public enum StandaardKaartenSet {

	DIRECTIE_MANAGEMENT(
		new KaartenSet(
			1L,
			StandaardRol.DIRECTIE_MANAGEMENT.getRol(),
			Set.of(
				new Kaart(1, "Wij gaan effectief met projecten om."),
				new Kaart(2, "Alle projecten leveren resultaten waar je op langere termijn wat aan hebt."),
				new Kaart(3, "Bij de keuze van projecten nemen we het grote geheel mee."),
				new Kaart(4, "Wij hebben goed overzicht over alle projecten."),
				new Kaart(5, "Wij hebben projectleiders in huis die de projecten tot een goed einde brengen."),
				new Kaart(6, "We leren van onze ervaringen met projecten."),
				new Kaart(7, "We hebben allemaal dezelfde verwachting van de toekomst van onze organisatie."),
				new Kaart(8, "We stemmen de bemensing af op de complexiteit van de projecten."),
				new Kaart(9, "We houden rekening met de belanghebbenden in de keuze van onze projecten."),
				new Kaart(10, "ledereen neemt hier verantwoordelijkheid op basis van heldere afspraken."),
				new Kaart(11, "We halen spin-off uit onze projecten en we leren van onze ervaringen."),
				new Kaart(12, "In onze organisatie stemmen we voldoende af voor we aan de slag gaan."),
				new Kaart(13, "We hebben onze medewerkers goed toegerust voor hun taak en het werken als team."),
				new Kaart(14, "Onze projecten dragen bij aan de missie en strategie van de organisatie."),
				new Kaart(15, "We hebben voldoende uitgewerkt wie wat moet bijdragen om samen effectief te zijn."),
				new Kaart(16, "We stellen in onze organisatie heldere deadlines."),
				new Kaart(17, "We hebben de projecten goed afgestemd op wat onze organisatie aan kan."),
				new Kaart(18, "We hebben goed in beeld hoeveel waarde onze projecten opleveren."),
				new Kaart(19, "Projecten worden zonodig bijgesteld, waarbij we het belang van de eindgebruikerin het oog houden."),
				new Kaart(20, "We zijn ons er van bewust dat we elkaar nodig hebben om succesvol te zijn."),
				new Kaart(21, "Onze mensen zijn voldoende ondernemend."),
				new Kaart(22, "We geven het goede voorbeeld."),
				new Kaart(23, "Bij ons stellen we ook gevoelige zaken aan de orde."),
				new Kaart(24, "We passen ons aan als dat nodig is."),
				new Kaart(25, "Eigen invulling.")
			)
		)
	),
	OPDRACHTGEVER(
		new KaartenSet(
			2L,
			StandaardRol.OPDRACHTGEVER.getRol(),
			Set.of(
				new Kaart(1, "Een gestructureerde aanpak helpt me om succesvol nieuwe dingen te ontwikkelen."),
				new Kaart(2, "Als ik een opdracht geef, besteed ik voldoende tijd aan het briefen van de projectleider."),
				new Kaart(3, "Het is duidelijk welke prioriteit dit project heeft binnen de organisatie."),
				new Kaart(4, "lk kan de projecten die ik opstart, goed overzien."),
				new Kaart(5, "De projectleiders brengen mijn projecten tot een goed einde."),
				new Kaart(6, "We leren van onze ervaringen met projecten."),
				new Kaart(7, "Alle betrokkenen weten wat het project op lange termijn gaat opleveren."),
				new Kaart(8, "De bemensing van mijn projecten is afgestemd op de complexiteit ervan."),
				new Kaart(9, "Bij belangrijke besluiten betrekken we de belanghebbenden, waaronder de eindgebruiker."),
				new Kaart(10, "Mijn projecten worden binnen budget en tijd afgerond."),
				new Kaart(11, "We leren van onze fouten, zodat projecten steeds beter aangepakt worden."),
				new Kaart(12, "We investeren voldoende in de voorbereiding van onze projecten."),
				new Kaart(13, "De medewerkers zijn goed toegerust voor hun taak en het werken met teams."),
				new Kaart(14, "De project- resultaten dragen duidelijk bij aan onze doelstellingen."),
				new Kaart(15, "De projectleiders maken een goede analyse van hun projecten."),
				new Kaart(16, "Ik kan erop vertrouwen dat deadlines gerespecteerd worden."),
				new Kaart(17, "Mijn projecten zijn afgestemd op de beschikbaarheid van projectleiders."),
				new Kaart(18, "lk ben er zeker van dat de eindgebruiker blij zal zijn met de resultaten."),
				new Kaart(19, "Mijn projecten lopen volgens planning en de eindgebruikers zijn tevreden."),
				new Kaart(20, "We gaan niet voor onszelf, maar voor het gezamenlijk succes."),
				new Kaart(21, "De projectleiders gaan goed en zelfstandig met problemen om."),
				new Kaart(22, "We stimuleren elkaar om projecten tot een succes te maken."),
				new Kaart(23, "We bespreken vervelende boodschappen met ieder die het aangaat."),
				new Kaart(24, "ledereen is bereid naar zichzelf te kijken als dat nodig is."),
				new Kaart(25, "Eigen invulling.")
			)
		)
	),
	GEBRUIKER_EINDRESULTAAT(
		new KaartenSet(
			3L,
			StandaardRol.GEBRUIKER_EINDRESULTAAT.getRol(),
			Set.of(
				new Kaart(1, "In onze organisatie ben je zeker van effectieve verbetering en vernieuwing."),
				new Kaart(2, "Het is ons duidelijk wat we aan dit project hebben."),
				new Kaart(3, "lk begrijp dat ze dit project gekozen hebben."),
				new Kaart(4, "lk heb vertrouwen dat ze de projecten netjes afronden."),
				new Kaart(5, "De projectleiders krijgen hun projecten goed voor elkaar."),
				new Kaart(6, "lk merk regelmatig dat er iets verbeterd is in de aanpak van projecten."),
				new Kaart(7, "Het is mij duidelijk waar dit project toe leidt."),
				new Kaart(8, "Ik vind de project- medewerkers professioneel."),
				new Kaart(9, "De project- medewerkers betrekken mij goed bij het project."),
				new Kaart(10, "Het is mij duidelijk wat dit project voor mij betekent."),
				new Kaart(11, "Het projectteam vraagt mij naar mijn ervaringen."),
				new Kaart(12, "Ze stellen mij de goede vragen voor ze beginnen."),
				new Kaart(13, "lk heb vertrouwen in de kundigheid en de samenwerking in dit project."),
				new Kaart(14, "We hebben echt wat aan de projectresultaten."),
				new Kaart(15, "Ik merk dat iedereen weet wat ze aan het doen zijn."),
				new Kaart(16, "We kunnen erop rekenen dat we het resultaat op tijd krijgen."),
				new Kaart(17, "lk merk dat de projectmedewerkers met aandacht hun werk doen."),
				new Kaart(18, "Als ik wil, kan ik op ieder moment nagaan of ze aan onze verwachtingen zullen voldoen."),
				new Kaart(19, "Tijdens het hele project luisteren de projectmedewerkers goed naar ons."),
				new Kaart(20, "Het is duidelijk dat ze dit samen met ons willen doen."),
				new Kaart(21, "Op een verantwoorde manier komen ze met werkbare alternatieven."),
				new Kaart(22, "Ze doen wat ze zeggen."),
				new Kaart(23, "Ze nodigen je uit om eerlijk te zeggen wat je er van vindt."),
				new Kaart(24, "Als ze ons daarmee beter kunnen bedienen, dan passen ze zich aan."),
				new Kaart(25, "Eigen invulling.")
			)
		)
	),
	PROJECTLEIDER(
		new KaartenSet(
			4L,
			StandaardRol.PROJECTLEIDER.getRol(),
			Set.of(
				new Kaart(1, "We hanteren een projectmethodiek waar we wat aan hebben."),
				new Kaart(2, "lk zie duidelijk dat het project nuttig en gewenst is."),
				new Kaart(3, "Het is mij duidelijk dat het een voor de organisatie relevant project is."),
				new Kaart(4, "lk kan de projecten die ik heb lopen, goed overzien."),
				new Kaart(5, "Het gaat me zonder meer lukken om dit project te leiden."),
				new Kaart(6, "Het management neemt mijn aanbevelingen voor de toekomst serieus."),
				new Kaart(7, "Het is mij duidelijk wat de opdrachtgever met dit project wil bereiken."),
				new Kaart(8, "We zijn goed opgewassen tegen de complexiteit van onze opdracht."),
				new Kaart(9, "lk kan de belangen van de verschillende partijen goed in evenwicht brengen."),
				new Kaart(10, "De projectopdracht biedt mij houvast in de afstemming met mijn opdrachtgever en mijn team."),
				new Kaart(11, "Mijn project- evaluaties leveren echt iets op."),
				new Kaart(12, "Wij nemen voldoende tijd voor voorbereiding en overleg."),
				new Kaart(13, "We besteden voldoende tijd aan opleiding."),
				new Kaart(14, "Met ons projectresultaat helpen we de eindgebruiker."),
				new Kaart(15, "Ik heb de scope van het project goed afgesproken met mijn opdrachtgever."),
				new Kaart(16, "lk heb een uitdagende, maar realistische deadline."),
				new Kaart(17, "lk kan steeds over de juiste mensen beschikken."),
				new Kaart(18, "Wij weten dat wij zullen voldoen aan de verwachtingen van de eindgebruiker."),
				new Kaart(19, "lk besteed voldoende tijd aan rapportage en evaluatie."),
				new Kaart(20, "Mijn projectteam levert samen het resultaat op."),
				new Kaart(21, "Mijn opdrachtgever en ik gaan wijzigingen en risico's niet uit de weg."),
				new Kaart(22, "lk zorg dat mijn team productief en plezierig samenwerkt."),
				new Kaart(23, "Mijn opdrachtgever, mijn team en ik gaan lastige situaties niet uit de weg."),
				new Kaart(24, "We luisteren naar anderen en doen wat met die feedback."),
				new Kaart(25, "Eigen invulling.")
			)
		)
	),
	PROJECTTEAMLID(
		new KaartenSet(
			5L,
			StandaardRol.PROJECTTEAMLID.getRol(),
			Set.of(
				new Kaart(1, "Het is duidelijk aan wie ik waarvoor verantwoording afleg."),
				new Kaart(2, "De eindgebruiker heeft baat bij het resultaat van dit project."),
				new Kaart(3, "Het is duidelijk welke prioriteit dit project heeft binnen de organisatie."),
				new Kaart(4, "Onze projectleider stelt de goede prioriteiten."),
				new Kaart(5, "lk heb vertrouwen in de kundigheid van de projectleider."),
				new Kaart(6, "In dit project gebruiken we ervaringen uit eerdere projecten."),
				new Kaart(7, "De uiteindelijke opbrengst van dit project is mij duidelijk."),
				new Kaart(8, "De complexiteit van ons project is vooraf goed ingeschat."),
				new Kaart(9, "Wij krijgen de verschillende belangen voldoende op één lijn."),
				new Kaart(10, "We hebben een duidelijk vertrekpunt voor dit project."),
				new Kaart(11, "Onze evaluaties leveren echte verbeteringen op."),
				new Kaart(12, "In de voorbereidingsfase is er voldoende ruimte voor onze bijdragen."),
				new Kaart(13, "We werken als een vakkundig team goed samen."),
				new Kaart(14, "Het is duidelijk dat ons project nut heeft."),
				new Kaart(15, "Het is iedereen duidelijk hoe zijn bijdrage in het grotere geheel past."),
				new Kaart(16, "We hebben er vertrouwen in dat we onze deadlines gaan halen."),
				new Kaart(17, "We kunnen dit project er goed bij doen, ondanks onze andere verplichtingen."),
				new Kaart(18, "Het is duidelijk dat wij de afspraken met de eindgebruiker nakomen."),
				new Kaart(19, "Wij stemmen voldoende af met de projectleider en de eindgebruiker."),
				new Kaart(20, "Het is iedereen duidelijk dat we elkaar nodig hebben om succes te bereiken."),
				new Kaart(21, "Verrassingen zijn niet altijd leuk, maar we gaan er goed mee om."),
				new Kaart(22, "Ik werk met plezier aan dit project."),
				new Kaart(23, "Feedback geven spreekt in dit project vanzelf, ook al is het soms best moeilijk."),
				new Kaart(24, "Feedback geven brengt ons verder in dit team."),
				new Kaart(25, "Eigen invulling.")
			)
		)
	),
	EXTERN_PROJECTTEAMLID(
		new KaartenSet(
			6L,
			StandaardRol.EXTERN_PROJECTTEAMLID.getRol(),
			Set.of(
				new Kaart(1, "De klant geeft mij voldoende armslag om een goede bijdrage te leveren."),
				new Kaart(2, "Ik begrijp de noodzaak van dit project voor mijn klant."),
				new Kaart(3, "lk zie dat mijn klant met dit project een goede keuze heeft gemaakt."),
				new Kaart(4, "Ik kan dit project er goed bij doen."),
				new Kaart(5, "lk heb vertrouwen in de kundigheid van de projectleider."),
				new Kaart(6, "Ook ik draag bij aan de professionaliteit van mijn klant."),
				new Kaart(7, "De uiteindelijke opbrengst van dit project is mij duidelijk."),
				new Kaart(8, "Mijn klant heeft met mij de juiste keuze gedaan."),
				new Kaart(9, "lk heb voldoende ruimte voor mijn andere belangen buiten dit project."),
				new Kaart(10, "We hebben een duidelijk vertrekpunt voor dit project."),
				new Kaart(11, "lk lever mijn bijdrage aan de evaluatie van het project."),
				new Kaart(12, "In de voorbereidingsfase is er voldoende ruimte voor mijn bijdrage."),
				new Kaart(13, "De ontwikkeling van het projectteam krijgt voldoende aandacht."),
				new Kaart(14, "lk weet zeker dat het project en mijn bijdrage nut hebben."),
				new Kaart(15, "Het is iedereen duidelijk hoe zijn bijdrage in het grotere geheel past."),
				new Kaart(16, "We hebben er vertrouwen in dat we onze deadlines gaan halen."),
				new Kaart(17, "Ik kan dit project er goed bij doen, ondanks mijn andere verplichtingen."),
				new Kaart(18, "lk weet zeker dat mijn klant en mijn eigen organisatie tevreden zullen zijn met het resultaat."),
				new Kaart(19, "lk stem voldoende af met de projectleider en de eindgebruiker."),
				new Kaart(20, "Het is iedereen duidelijk dat we elkaar nodig hebben om succes te bereiken."),
				new Kaart(21, "Risico's horen erbij en vangen we in goed overleg af."),
				new Kaart(22, "Niemand beschouwt mij als 'de externe'."),
				new Kaart(23, "Bij deze klant stellen we aan de orde wat besproken moet worden, ook als dat gevoelig ligt."),
				new Kaart(24, "Feedback geven heeft zin: we passen ons aan als dat nodig is."),
				new Kaart(25, "Eigen invulling.")
			)
		)
	),
	PROJECTOMGEVING(
		new KaartenSet(
			7L,
			StandaardRol.PROJECTOMGEVING.getRol(),
			Set.of(
				new Kaart(1, "Met de methodiek die ze hier hanteren, krijgen ze de projecten goed voor elkaar."),
				new Kaart(2, "Ook ik heb baat bij de resultaten van dit project."),
				new Kaart(3, "lk begrijp waarom voor dat project gekozen is."),
				new Kaart(4, "lk weet het als er projecten lopen die mijn werkzaamheden raken."),
				new Kaart(5, "lk heb er vertrouwen in dat de projectleider ook met mij rekening houdt."),
				new Kaart(6, "lk merk regelmatig dat er iets verbeterd isin de aanpak van projecten."),
				new Kaart(7, "lk weet wat dat project oplevert."),
				new Kaart(8, "Men kiest hier de goede projectleider voor ieder project."),
				new Kaart(9, "De projectleider ziet ook mijn belangen."),
				new Kaart(10, "lk begrijp wat dit project voor hen betekent."),
				new Kaart(11, "lk zie dat projectevaluaties leiden tot verbeteringen."),
				new Kaart(12, "De projectleider informeert mij over voortgang en plannen."),
				new Kaart(13, "Het projectteam is een deskundig team."),
				new Kaart(14, "Projectresultaten dragen duidelijk bij aan onze doelstellingen."),
				new Kaart(15, "Ze weten waar ze mee bezig zijn in die projecten."),
				new Kaart(16, "De deadlines van dat project zijn voor mij werkbaar."),
				new Kaart(17, "We kunnen dat project er goed bij hebben, ondanks onze andere verplichtingen."),
				new Kaart(18, "lk weet dat de organisatie met het resultaat zal kunnen werken."),
				new Kaart(19, "Als ik suggesties doe, zie ik dat daarmee wat wordt gedaan."),
				new Kaart(20, "Dat team werkt geolied samen."),
				new Kaart(21, "De projectleider reageert goed op de verrassingen die het project met zich meebrengt."),
				new Kaart(22, "Ze doen wat ze zeggen."),
				new Kaart(23, "De projectleider vraagt goed door en gaat lastige situaties niet uit de weg."),
				new Kaart(24, "Als ik de projectleider ergens op aanspreek, dan doet hij er echt iets mee."),
				new Kaart(25, "Eigen invulling.")
			)
		)
	);

	private final KaartenSet kaartenSet;

	StandaardKaartenSet(KaartenSet kaartenSet) {
		this.kaartenSet = kaartenSet;
	}

	public KaartenSet getKaartenSet() {
		return this.kaartenSet;
	}

}
