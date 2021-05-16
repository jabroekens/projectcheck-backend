# Voordat je begint
Voordat je aan de slag gaat, zijn er een aantal afspraken:
1. Maak via JIRA een featurebranch aan voor de hoofdtaak waaraan jij werkt genaamd: `feature/<issueCode>-<useCaseNaam>` (bijv. `feature/OGOVQN-1-Aanmaken-room`).
    * **Let op**: de featurebranch heeft dus de naam van de usecase die hoort bij de hoofdtaak, dit zorgt voor *traceerbaarheid* in de ontwerpen.
    * Het is niet erg als de branchnaam te lang is, zolang duidelijk is welke functionaliteit de branch implementeert.
2. Haal regelmatig veranderingen op uit de `dev`-branch (`git pull --rebase origin/dev`)
    * Zorg altijd dat bij het pullen de `rebase` optie is meegegeven!
3. Als je klaar denkt te zijn, maak een pull request aan om met de `dev` branch te mergen.
    * Zorg dat je code voldoet aan de Definition of Done zoals aangegeven op Confluence.

## Algemene flow
1. De client verstuurt een verzoek met een begeleidernaam naar AppService om een kamer aan te maken.
2. AppService vraag aan KamerRepository om een kamer aan te maken met de meegegeven begeleidernaam.
3. KamerRepository maakt een kamer aan (incl. begeleider), legt deze vast met een DAO en geeft deze terug aan AppService.
4. AppService registreert de kamercode bij KamerService en geeft de WebSocket URL terug aan de client.
5. De client stelt een verbinding op met de WebSocket blootgesteld door KamerService.
5. De client stuurt een event over de WebSocket-connectie naar KamerService.
6. KamerService voert het event uit en stuurt een event response naar de client.

Een event is een JSON-bericht. Elk event heeft minimaal de volgende waarden:
```
{
    "event":"EVENT",
    "deelnemerId": {
        "id": 1,
        "kamerCode": "123456"
    }
}
```
Waarbij `EVENT`, `1` en `123456` waarden zijn die de client meegeeft.

Een event response is een JSON-bericht. Een event response heeft de volgende waarden:
```
{
    "status": "STATUS",
    "context": {},
    "datum": "DATUM",
    "antwoordOp": "EVENT"
}
```
Waarbij `STATUS`, `DATUM`, `EVENT` waarden zijn die de back-end meegeeft.
Het veld `antwoordOp` is optioneel en wordt mogelijk niet meegegeven.

## Codestijl
Om de kwaliteit van de code te waarborgen, wordt er een vaste stijl vastgehouden in de code.
Voor het gemak is er een [Intellij IDEA code style XML](https://confluenceoosevt.aimsites.nl/download/attachments/238631614/Code-Style_De_ProjectCheck.xml) beschikbaar gesteld die gebruikt kan worden.
De java code zal voldoen aan de stijling zoals geschreven in [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html), met de volgende aanpassingen:

#### 2.3.1 Whitespace characters
Aside from the line terminator sequence, the ASCII horizontal space character (0x20) **and horizontal tab character (0x09)** are the only whitespace characters that appears anywhere in a source file. This implies that:

1. All other whitespace characters in string and character literals are escaped.
2. **Tab characters are used for indentation, with space characters for alignment.**

##### 3.4.2.2 Getters and setters: never split
**When a class has a getter and setter for a field, these appear together, with no other code in between (not even private members).**

### 4.2 Block indentation: +1 tab
Each time a new block or block-like construct is opened, the indent increases by **one tab**. When the block ends, the indent returns to the previous indent level. The indent level applies to both code and comments throughout the block. (See the example in Section 4.1.2, [Nonempty blocks: K & R Style.](#s4.1.2-blocks-k-r-style))

### 4.4 columns limit: 180
Java code has a column limit of **180 characters**. A "character" means any Unicode code point. Except as noted below, any line that would exceed this limit must be line-wrapped, as explained in Section 4.5, [Line-wrapping](#s4.5-line-wrapping).

#### 4.5.2 Indent continuation lines at same indent level, align with spaces
When line-wrapping, each line after the first (each continuation line) is indented at **the same level as the original line and aligned as necessary with spaces**.

When there are multiple continuation lines, **alignment may be varied as desired**. In general, two continuation lines use the same indentation level if and only if they begin with syntactically parallel elements.

Section 4.6.3 on [Horizontal alignment](#s4.6.3-horizontal-alignment) addresses the discouraged practice of using a variable number of spaces to align certain tokens with previous lines.

#### 4.6.1 Vertical Whitespace
A single blank line always appears:

1. *Between* consecutive members or initializers of a class: fields, constructors, methods, nested classes, static initializers, and instance initializers.
    * **Exception:** A blank line between two consecutive fields (having no other code between them) is optional. Such blank lines are used as needed to create *logical groupings* of fields.
    * **Exception:** Blank lines between enum constants are covered in [Section 4.8.1](#s4.8.1-enum-classes).
2. As required by other sections of this document (such as Section 3, [Source file structure](#s3-source-file-structure), and Section 3.3, [Import statements](#s3.3-import-statements)).
3. **Before the first member or initializer, and after the last member or initializer of the class.**

A single blank line may also appear anywhere it improves readability, for example between statements to organize the code into logical subsections.

**Multiple consecutive blank lines are not permitted.**

##### 4.8.1.1 Indentation
**The contents of a switch block are indented at the same level as the switch statement**

After a switch label, there is a line break, and the indentation level is increased **+1 tab**, exactly as if a block were being opened. The following switch label returns to the previous indentation level, as if a block had been closed.

#### 5.2.1 Package names
**Package names follow Oracle's [package naming conventions](https://docs.oracle.com/javase/tutorial/java/package/namingpkgs.html).**

### Voorbeelden
Zie de bestanden onderstaande bestanden voor voorbeelden:
* `src/main/java/nl/han/oose/buizerd/projectcheck_backend/Util.java`
* `src/main/java/nl/han/oose/buizerd/projectcheck_backend/domain/Begeleider.java`
* `src/main/java/nl/han/oose/buizerd/projectcheck_backend/domain/DeelnemerId.java`
* `src/main/java/nl/han/oose/buizerd/projectcheck_backend/domain/Deelnemer.java`
* `src/main/java/nl/han/oose/buizerd/projectcheck_backend/domain/Kamer.java`
* `src/main/java/nl/han/oose/buizerd/projectcheck_backend/repository/KamerRepository.java`

## Testen
Er wordt gebruik gemaakt van JUnit5 en Mockito. Bij het schrijven van tests is het noodzakelijk dat alle *dependencies* gemockt worden.
Zie de bestanden onderstaande bestanden voor voorbeelden:
* `src/test/java/nl/han/oose/buizerd/projectcheck_backend/UtilTest.java`
* `src/test/java/nl/han/oose/buizerd/projectcheck_backend/domain/BegeleiderTest.java`
* `src/test/java/nl/han/oose/buizerd/projectcheck_backend/domain/DeelnemerIdTest.java`
* `src/test/java/nl/han/oose/buizerd/projectcheck_backend/domain/DeelnemerTest.java`
* `src/test/java/nl/han/oose/buizerd/projectcheck_backend/domain/KamerTest.java`
* `src/test/java/nl/han/oose/buizerd/projectcheck_backend/repository/KamerRepositoryTest.java`

## Aan de slag
Nu dat je een idee hebt van de procesgang, codestijl en de tooling, kun je aan de slag.
**Maar let op**: events die ontvangen worden op een WebSocket-verbinding moeten op een specifieke manier uitgewerkt worden, zie hieronder:

### Events
Om een event aan te maken, maak je een klasse aan die `Event` extend genaamd `FooEvent` (waarbij `Foo` vervangen kan worden).
Je kunt een event ook waardes geven, in de vorm van instantievelden. Als een veld verplicht is, dan markeer je deze met `@NotNull`.
De velden worden gevalideerd doormiddel van Bean Validation, dus je kunt elke mogelijke validatie hiervoor gebruiken.

### Voorbeeld
ChatEvent.java
```
public class ChatEvent extends Event {

	// package-private zodat het getest kan worden.
	@NotNull
	String woord;

	@Override
	protected EventResponse voerUit(Kamer kamer, Session session) {
		Optional<Deelnemer> deelnemer = kamer.getDeelnemer(super.getDeelnemerId());
		if (deelnemer.isPresent()) {
			String bericht = String.format("%s zegt: \"%s\"", deelnemer.get().getNaam(), woord);
			super.stuurNaarAlleClients();
			return new EventResponse(EventResponse.Status.OK).metContext("bericht", bericht);
		} else {
			return new EventResponse(EventResponse.Status.DEELNEMER_NIET_GEVONDEN)
				.metContext("deelnemerId", super.getDeelnemerId());
		}
	}

}
```

ChatEventTest.java
```
@ExtendWith(MockitoExtension.class)
public class ChatEventTest {

	private ChatEvent chatEvent;

	@BeforeEach
	void setUp() {
		chatEvent = new ChatEvent();
		chatEvent.woord = "Hallo!";
	}

	@Test
	void voerUit_deelnemerAanwezig(@Mock Kamer kamer, @Mock Session session, @Mock Deelnemer deelnemer) {
		Mockito.when(kamer.getDeelnemer(chatEvent.getDeelnemerId())).thenReturn(Optional.of(deelnemer));
		String expectedBericht = String.format("%s zegt: \"%s\"", deelnemer.getNaam(), chatEvent.woord);

		EventResponse response = chatEvent.voerUit(kamer, session);

		Assertions.assertAll(
			() -> Mockito.verify(kamer).getDeelnemer(chatEvent.getDeelnemerId()),
			() -> Assertions.assertTrue(chatEvent.stuurNaarAlleClients),
			() -> Assertions.assertEquals(EventResponse.Status.OK, response.status),
			() -> Assertions.assertEquals(expectedBericht, response.context.get("bericht"))
		);
	}

	@Test
	void voerUit_deelnemerAfwezig(@Mock Kamer kamer, @Mock Session session) {
		Mockito.when(kamer.getDeelnemer(chatEvent.getDeelnemerId())).thenReturn(Optional.empty());

		EventResponse response = chatEvent.voerUit(kamer, session);

		Assertions.assertAll(
			() -> Mockito.verify(kamer).getDeelnemer(chatEvent.getDeelnemerId()),
			() -> Assertions.assertEquals(EventResponse.Status.DEELNEMER_NIET_GEVONDEN, response.status),
			() -> Assertions.assertEquals(chatEvent.getDeelnemerId(), response.context.get("deelnemerId"))
		);
	}

}
```
