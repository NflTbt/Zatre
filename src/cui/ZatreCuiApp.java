package cui;

import static domein.DomeinRegels.MAX_LEEFTIJD;
import static domein.DomeinRegels.MIN_GEBRUIKERSNAAM_LENGTE;
import static domein.DomeinRegels.MIN_LEEFTIJD;

import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import domein.DomeinController;
import domein.DomeinRegels;
import exceptions.DubbeleSpelerException;
import exceptions.GeenSpeelkansenException;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
import exceptions.PlayerExistsException;
import exceptions.SpelerLijstLeegException;
import exceptions.SpelerLijstTeGrootException;
import exceptions.SpelerLijstTeKleinException;
import exceptions.SpelerNietInLijstException;
import taal.Taal;
import taal.Talen;

/**
 * CUI applicatie voor het uitvoeren van Zatre.
 * 
 * @author Naoufal Thabet
 * @author Tom Coenen
 *
 */
public class ZatreCuiApp {

    private final DomeinController domeinController;
    private Scanner invoer = new Scanner(System.in);
    private Talen taal;

    /**
     * Constructor van de CLI app.
     * 
     * @param domeinController Laat toe te communiceren met domein
     */
    public ZatreCuiApp(DomeinController domeinController) {
	this.domeinController = domeinController;
    }

    /**
     * Main methode die de applicatie opstart, bevat logische sequence van de CUI.
     */
    public void start() {

	toonTaalMenu();
	toonHoofdMenu();

	invoer.close();
    }

    /**
     * Submethode die hoofdmenu afbeelt.
     */
    private void toonHoofdMenu() {
	int keuze = -1;
	do {

	    System.out.println(Taal.getString(taal, "cuiMaakKeuze"));
	    System.out.println("1. " + Taal.getString(taal, "cuiKeuzeSpelerSelecteren"));
	    System.out.println("2. " + Taal.getString(taal, "cuiKeuzeSpelerRegistreren"));
	    System.out.println("3. " + Taal.getString(taal, "cuiKeuzeStop"));
	    try {
		keuze = invoer.nextInt();
		switch (keuze) {
		case 1 -> startSelectieSpeler();
		case 2 -> startRegistratie();
		case 3 -> System.exit(0);
		default -> throw new InputMismatchException();
		}
	    } catch (InputMismatchException e) {
		System.out.println(Taal.getString(taal, "cuiFoutmeldingOngeldigeKeuze"));
		invoer.nextLine();
	    }

	} while (keuze != 3);

    }

    /**
     * Aanroepen om registratiemenu te starten.
     * 
     */
    private void startRegistratie() {
	String gebruikersnaam;
	LocalDate geboortejaar = LocalDate.now();
	int invoerGeboortejaar;
	invoer.nextLine();

	// Blijf in de loop tot all voorwaarden voldaan zijn voor registratie
	do {

	    // Vraag gebruikersnaam
	    do {
		try {
		    System.out.println(Taal.getString(taal, "cuiRegistratieGebruikersnaam", MIN_GEBRUIKERSNAAM_LENGTE));
		    gebruikersnaam = invoer.nextLine();

		    // Check ingevoerde username: Niet te kort.
		    if (gebruikersnaam.trim().length() < MIN_GEBRUIKERSNAAM_LENGTE) {
			throw new IllegalArgumentException(
				Taal.getString(taal, "cuiFoutmeldingGebruikersnaamTeKort", MIN_GEBRUIKERSNAAM_LENGTE));
		    }
		    break;
		} catch (IllegalArgumentException ex) {
		    System.out.println(ex.getMessage());
		}
	    } while (true);

	    // Vraag geboortejaar
	    do {
		try {
		    System.out.println(Taal.getString(taal, "cuiRegistratieGeboortejaar", MIN_LEEFTIJD));
		    invoerGeboortejaar = invoer.nextInt();
		    geboortejaar = LocalDate.of(invoerGeboortejaar, 1, 1);

		    // Check ingevoerde leeftijd: Niet te jong.
		    if (geboortejaar.plusYears(MIN_LEEFTIJD).isAfter(LocalDate.now())) {
			throw new IllegalArgumentException(Taal.getString(taal, "cuiFoutmeldingTeJong"));
		    }
		    // Check ingevoerde leeftijd: Niet te oud.
		    if (geboortejaar.isBefore(LocalDate.now().minusYears(MAX_LEEFTIJD))) {
			throw new IllegalArgumentException(Taal.getString(taal, "cuiFoutmeldingTeOud", MAX_LEEFTIJD));
		    }
		    break;
		} catch (InputMismatchException e) {
		    System.out.println(Taal.getString(taal, "FoutmeldingScannerOngeldigeInvoer"));
		    invoer.nextLine();
		} catch (IllegalArgumentException ex) {
		    System.out.println(ex.getMessage());
		    invoer.nextLine();
		}
	    } while (true);

	    // Registreer nieuwe speler
	    try {
		domeinController.registreer(gebruikersnaam, geboortejaar);

		System.out.printf("%s, %d %s ", domeinController.geefGebruikersnaam(),
			domeinController.geefSpeelkansen(), Taal.getString(taal, "cuiSpeelkansen"));
		// Breek uit de loop bij succesvolle registratie
		break;
	    } catch (PlayerExistsException e) {
		System.out.println(Taal.getString(taal, "FoutmeldingSQLSpelerBestaatAl"));
		invoer.nextLine();
	    } catch (OngeldigeKaraktersException e) {
		System.out.println(Taal.getString(taal, "FoutmeldingGeblokkeerdeKarakters",
			(Object[]) DomeinRegels.BLOCKED_CHARACTERS));
		invoer.nextLine();
	    }
	} while (true);

    }

    /**
     * Methode die gebruiker een taalkeuze toont. Initialiseert de correcte resource
     * bundle.
     */
    private void toonTaalMenu() {
	int keuze = -1;

	StringBuilder builder = new StringBuilder("Kies je taal - Choose your language - Choisissez votre langue:\n");

	builder.append(String.format("%s%n", "1. Voor Nederlands"));
	builder.append(String.format("%s%n", "2. For English"));
	builder.append(String.format("%s%n", "3. Pour le français"));
	String uitvoer = builder.toString();

	String foutmelding = "Kies een nummer tussen 1 en 3 - Choose a number between 1 and 3 - Choisissez un chiffre entre 1 et 3\n";

	do {
	    try {
		System.out.printf(uitvoer);
		keuze = invoer.nextInt();
		if (keuze < 0 || keuze > 3) {
		    throw new IllegalArgumentException(foutmelding);
		}
	    } catch (InputMismatchException e) {
		System.out.println(foutmelding);
		invoer.nextLine();
	    } catch (IllegalArgumentException ex) {
		System.out.println(ex.getMessage());
	    }
	} while (keuze < 0 || keuze > 3);

	switch (keuze) {
	case 1 -> setTaal(Talen.NL);
	case 2 -> setTaal(Talen.EN);
	case 3 -> setTaal(Talen.FR);
	}
	invoer.nextLine();

    }

    /**
     * Private methode die de lokale taal instelt. Deze wordt doorgegeven naar de
     * resourcebundle.
     * 
     * @param taal Een ENUM uit Talen
     */
    private void setTaal(Talen taal) {
	this.taal = taal;
    }

    /**
     * Submenu die toelaat spelers te selecteren en verwijderen voor het spelen van
     * een spel.
     */
    private void startSelectieSpeler() {
	int keuzeSelectie = -1;
	do {
	    System.out.println(Taal.getString(taal, "cuiMaakKeuze"));
	    System.out.println("1. " + Taal.getString(taal, "cuiKeuzeToevoegen"));
	    System.out.println("2. " + Taal.getString(taal, "cuiKeuzeVerwijderen"));
	    System.out.println("3. " + Taal.getString(taal, "cuiKeuzeVolgende"));
	    try {
		keuzeSelectie = invoer.nextInt();
		switch (keuzeSelectie) {
		case 1 -> {
		    try {
			System.out.println(Taal.getString(taal, "cuiGeefSpelerNaam"));
			String naam = invoer.next();
			System.out.println(Taal.getString(taal, "cuiGeefSpelerGeboortejaar"));
			LocalDate geboortejaar = LocalDate.of(invoer.nextInt(), 1, 1);
			checkLeeftijd(geboortejaar);
			selecteerSpeler(naam, geboortejaar);
		    } catch (OngeldigeKaraktersException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingGeblokkeerdeKarakters"));
			invoer.nextLine();
		    } catch (DubbeleSpelerException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingDubbeleSpeler"));
			invoer.nextLine();
		    } catch (SpelerLijstTeGrootException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingSpelerlijstVol"));
			invoer.nextLine();
		    } catch (InputMismatchException e) {
			System.out.println(Taal.getString(taal, "cuiFoutmeldingOngeldigeKeuze"));
			invoer.nextLine();
		    } catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		    } catch (NoSuchPlayerException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingSQLNoSuchPlayer"));
			invoer.nextLine();
		    } catch (GeenSpeelkansenException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingSpeelkansenOp"));
			invoer.nextLine();
		    }
		}
		case 2 -> {
		    try {
			System.out.println(Taal.getString(taal, "cuiGeefSpelerNaam"));
			String naam = invoer.next();
			System.out.println(Taal.getString(taal, "cuiGeefSpelerGeboortejaar"));
			LocalDate geboortejaar = LocalDate.of(invoer.nextInt(), 1, 1);
			checkLeeftijd(geboortejaar);
			verwijderSpeler(naam, geboortejaar);
		    } catch (OngeldigeKaraktersException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingGeblokkeerdeKarakters"));
			invoer.nextLine();
		    } catch (InputMismatchException e) {
			System.out.println(Taal.getString(taal, "cuiFoutmeldingOngeldigeKeuze"));
			invoer.nextLine();
		    } catch (SpelerNietInLijstException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingSpelerNietActief"));
			invoer.nextLine();
		    } catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		    } catch (NoSuchPlayerException e) {
			System.out.println(Taal.getString(taal, "FoutmeldingSQLNoSuchPlayer"));
			invoer.nextLine();
		    }
		}
		case 3 -> {
		    if (domeinController.geefOverzichtSpelers().length < DomeinRegels.MIN_SPELERS) {
			throw new SpelerLijstTeKleinException();
		    }
		    printOverzicht();
		}
		default -> throw new InputMismatchException();
		}
	    } catch (InputMismatchException e) {
		System.out.println(Taal.getString(taal, "cuiFoutmeldingOngeldigeKeuze"));
		invoer.nextLine();
	    } catch (SpelerLijstTeKleinException e) {
		System.out.println(Taal.getString(taal, "FoutmeldingSpelerlijstTeKlein"));
		keuzeSelectie = -1;
		invoer.nextLine();
	    } catch (SpelerLijstLeegException e) {
		System.out.println(Taal.getString(taal, "FoutmeldingSpelerlijstLeeg"));
	    }

	} while (keuzeSelectie != 3);
    }

    /**
     * Checkt een leeftijd voor geldige waarde
     * 
     * @param geboortejaar het te checken geboortejaar
     * @throws IllegalArgumentException Indien ongeldig
     */
    private void checkLeeftijd(LocalDate geboortejaar) throws IllegalArgumentException {
	// Check ingevoerde leeftijd: Niet te jong.
	if (geboortejaar.plusYears(MIN_LEEFTIJD).isAfter(LocalDate.now())) {
	    throw new IllegalArgumentException(Taal.getString(taal, "cuiFoutmeldingTeJong"));
	}
	// Check ingevoerde leeftijd: Niet te oud.
	if (geboortejaar.isBefore(LocalDate.now().minusYears(MAX_LEEFTIJD))) {
	    throw new IllegalArgumentException(Taal.getString(taal, "cuiFoutmeldingTeOud", MAX_LEEFTIJD));
	}
    }

    /**
     * Geef een overzicht van spelers
     * 
     * @throws SpelerLijstLeegException Wordt gegooid indien spelerlijst leeg is.
     */
    private void printOverzicht() throws SpelerLijstLeegException {
	String[] spelerLijst = domeinController.geefOverzichtSpelers();
	StringBuilder builder = new StringBuilder();
	for (String speler : spelerLijst) {
	    String[] tokens = speler.split("#");
	    builder.append(String.format("Speler: %s, Geboortejaar: %s, Speelkansen: %s\n", tokens[0],
		    tokens[1].substring(0, 4), tokens[2]));
	}

	String out = builder.toString();
	System.out.print(out);
    }

    /**
     * Voegt een speler toe aan de actieve lijst
     * 
     * @param gebruikersnaam gebruikersnaam van de toe te voegen speler.
     * @param geboortejaar   geboortejaar van de toe te voegen speler.
     * @throws OngeldigeKaraktersException Exception die gegooid wordt indien een
     *                                     input naar het domein ongeldige karakters
     *                                     bevat, ter preventie van MYSQL en/of
     *                                     domeinissues.
     * @throws DubbeleSpelerException      Exception die gegooid wordt wanneer men
     *                                     probeert een speler aan de
     *                                     SpelerRepository toe te voegen die reeds
     *                                     in de lijst aanwezig is.
     * @throws SpelerLijstTeGrootException Exception die gegooid wordt wanneer de
     *                                     SpelerRepository reeds het maximum aantal
     *                                     spelers bevat, en er nog een speler wordt
     *                                     toegevoegd.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws GeenSpeelkansenException
     */
    private void selecteerSpeler(String gebruikersnaam, LocalDate geboortejaar) throws OngeldigeKaraktersException,
	    DubbeleSpelerException, SpelerLijstTeGrootException, NoSuchPlayerException, GeenSpeelkansenException {
	domeinController.voegSpelerToe(gebruikersnaam, geboortejaar);
    }

    /**
     * Verwijdert een speler van de actieve lijst
     * 
     * @param gebruikersnaam Gebruikersnaam van de te verwijderen speler.
     * @param geboortejaar   Geboortejaar van de te verwijderen speler.
     * @throws SpelerNietInLijstException  Exception die gegooid wordt wanneer men
     *                                     een speler probeert aan te spreken die
     *                                     niet in de repository lijst voorkomt.
     * @throws OngeldigeKaraktersException Exception die gegooid wordt indien een
     *                                     input naar het domein ongeldige karakters
     *                                     bevat, ter preventie van MYSQL en/of
     *                                     domeinissues.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws SpelerLijstLeegException
     */
    private void verwijderSpeler(String gebruikersnaam, LocalDate geboortejaar) throws SpelerNietInLijstException,
	    OngeldigeKaraktersException, NoSuchPlayerException, SpelerLijstLeegException {
	domeinController.verwijderSpeler(gebruikersnaam, geboortejaar);
    }
}
