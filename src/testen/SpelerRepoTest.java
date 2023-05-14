package testen;

import static domein.DomeinRegels.MAX_SPELERS;
import static domein.DomeinRegels.MIN_LEEFTIJD;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.Speler;
import domein.SpelerRepo;
import exceptions.DubbeleSpelerException;
import exceptions.GeenSpeelkansenException;
import exceptions.OngeldigeKaraktersException;
import exceptions.SpelerLijstLeegException;
import exceptions.SpelerLijstTeGrootException;

/**
 * JUNIT Test Case voor de klase SpelerRepo
 * 
 * @author Tom Coenen
 *
 */
class SpelerRepoTest {

    private static final String GELDIGE_GEBRUIKERSNAAM = "GeldigeNaam";
    private static final LocalDate GELDIGE_DATUM0 = LocalDate.now().minusYears(MIN_LEEFTIJD);
    private static final LocalDate GELDIGE_DATUM1 = LocalDate.now().minusYears(MIN_LEEFTIJD + 1);
    private static final LocalDate GELDIGE_DATUM2 = LocalDate.now().minusYears(MIN_LEEFTIJD + 2);
    private static final LocalDate GELDIGE_DATUM3 = LocalDate.now().minusYears(MIN_LEEFTIJD + 3);
    private static final LocalDate GELDIGE_DATUM4 = LocalDate.now().minusYears(MIN_LEEFTIJD + 4);
    private List<Speler> lijst;
    private SpelerRepo repo;

    /**
     * Creer een lokale lijst van 5 geldige speler objecten, aanmaken repo
     * 
     * @throws OngeldigeKaraktersException
     * 
     */
    @BeforeEach
    public void before() throws OngeldigeKaraktersException {
	repo = new SpelerRepo();
	lijst = new ArrayList<>();

	lijst.add(new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIGE_DATUM0));
	lijst.add(new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIGE_DATUM1));
	lijst.add(new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIGE_DATUM2));
	lijst.add(new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIGE_DATUM3));
	lijst.add(new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIGE_DATUM4));
    }

    /**
     * Check correct functioneren bij normale lengte lijst
     */
    @Test
    void voegSpelersToeAanLijst_geldigAantal_geenFouten() throws OngeldigeKaraktersException, DubbeleSpelerException,
	    SpelerLijstTeGrootException, GeenSpeelkansenException {

	for (int i = 0; i < MAX_SPELERS; i++) {
	    final int tempI = i;
	    Assertions.assertDoesNotThrow(() -> {
		repo.voegSpelerToe(
			new Speler(lijst.get(tempI).getGebruikersnaam(), lijst.get(tempI).getGeboortejaar()));
	    });
	}
    }

    /**
     * Check correcte exception wanneer lijst vol is
     */
    @Test
    void voegSpelersToeAanLijst_ongeldigAantal_throwsSLTGE() throws OngeldigeKaraktersException, DubbeleSpelerException,
	    SpelerLijstTeGrootException, GeenSpeelkansenException {
	for (int i = 0; i < MAX_SPELERS; i++) {
	    final int tempI = i;
	    Assertions.assertDoesNotThrow(() -> {
		repo.voegSpelerToe(
			new Speler(lijst.get(tempI).getGebruikersnaam(), lijst.get(tempI).getGeboortejaar()));
	    });
	}
	Assertions.assertThrows(SpelerLijstTeGrootException.class, () -> {
	    repo.voegSpelerToe(
		    new Speler(lijst.get(MAX_SPELERS).getGebruikersnaam(), lijst.get(MAX_SPELERS).getGeboortejaar()));
	});
    }

    /**
     * Check correct bijhouden en returnen van lijst
     */
    @Test
    void voegSpelersToeAanLijst_correcteReturnVanLijst() throws OngeldigeKaraktersException, DubbeleSpelerException,
	    SpelerLijstTeGrootException, GeenSpeelkansenException {
	// Vul de repo lijst
	for (int i = 0; i < MAX_SPELERS; i++) {
	    final int tempI = i;
	    Assertions.assertDoesNotThrow(() -> {
		repo.voegSpelerToe(
			new Speler(lijst.get(tempI).getGebruikersnaam(), lijst.get(tempI).getGeboortejaar()));
	    });

	    // Maak lokale lijst even lang
	    lijst = lijst.subList(0, MAX_SPELERS);

	    // check item per item of beide gelijk zijn adhv Speler.equals()
	    for (int j = 0; j < MAX_SPELERS; j++) {
		final int tempJ = i;
		Assertions.assertDoesNotThrow(() -> {
		    Assertions.assertTrue(repo.getGeselecteerdeSpelers().get(tempJ).equals(lijst.get(tempJ)));
		});
	    }

	}

    }

    /**
     * Voeg dubbele speler objecten toe, verwacht exception
     */
    @Test
    void voegDubbeleSpelerToe_throwsDubbeleSpelerException() throws OngeldigeKaraktersException, DubbeleSpelerException,
	    SpelerLijstTeGrootException, GeenSpeelkansenException {
	Assertions.assertDoesNotThrow(() -> {
	    repo.voegSpelerToe(new Speler(lijst.get(0).getGebruikersnaam(), lijst.get(0).getGeboortejaar()));
	});

	Assertions.assertThrows(DubbeleSpelerException.class, () -> {
	    repo.voegSpelerToe(new Speler(lijst.get(0).getGebruikersnaam(), lijst.get(0).getGeboortejaar()));
	});
    }

    /**
     * Probeer een lege lijst op te roepen, verwacht exception
     */
    @Test
    void vraagLegeLijstOp_ThrowsException() {

	Assertions.assertThrows(SpelerLijstLeegException.class, () -> {
	    repo.getGeselecteerdeSpelers();
	});

    }

    /**
     * Voeg een speler zonder speelkansen toe, check correcte exception gegooid.
     */
    @Test
    void voegSpelerZonderSpeelkansenToe_throwsException() {
	Assertions.assertThrows(GeenSpeelkansenException.class,
		() -> repo.voegSpelerToe(new Speler("SpelerMet0", LocalDate.of(1998, 1, 1), 0)));
    }
}