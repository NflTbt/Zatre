package testen;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.DomeinController;
import domein.Spel;
import domein.Speler;
import domein.SpelerRepo;
import exceptions.DubbeleSpelerException;
import exceptions.GeenSpeelkansenException;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
import exceptions.SpelNietAfgelopenException;
import exceptions.SpelerLijstLeegException;
import exceptions.SpelerLijstTeGrootException;
import exceptions.SpelerLijstTeKleinException;

/**
 * JUNIT Test Case voor de klasse Spel
 * 
 * @author Valentijn De Borggrave
 */

class SpelTest {

    private Spel spel;
    private SpelerRepo correcteRepo;

    /**
     * BeforeEach methode
     * 
     * Deze methode creert voor elke testfunctie een spelerrepo met 4 spelers en een
     * correct spel object
     * 
     * @throws OngeldigeKaraktersException
     * @throws GeenSpeelkansenException
     * @throws SpelerLijstTeGrootException
     * @throws DubbeleSpelerException
     * @throws NoSuchPlayerException
     * @throws SpelerLijstTeKleinException
     * @throws SpelerLijstLeegException
     */
    @BeforeEach
    public void before() throws DubbeleSpelerException, SpelerLijstTeGrootException, GeenSpeelkansenException,
	    OngeldigeKaraktersException, SpelerLijstLeegException, SpelerLijstTeKleinException, NoSuchPlayerException {
	correcteRepo = new SpelerRepo();

	correcteRepo.voegSpelerToe(new Speler("Omychron", LocalDate.of(1987, 1, 1)));
	correcteRepo.voegSpelerToe(new Speler("Naoufal", LocalDate.of(1985, 1, 1)));
	correcteRepo.voegSpelerToe(new Speler("Valentijn", LocalDate.of(1988, 1, 1)));
	correcteRepo.voegSpelerToe(new Speler("Boeing", LocalDate.of(1970, 1, 1)));

	spel = new Spel(new DomeinController(), correcteRepo);
    }

    /**
     * Constructor met correcte repo, geeft geen exceptions
     */

    @Test
    public void maakSpel_geeftGeenExceptions() {
	assertDoesNotThrow(() -> {
	    new Spel(new DomeinController(), correcteRepo);
	});
    }

    /**
     * Constructor spel zonder spelers moet SpelerLijstLeegException gooien
     */

    @Test
    public void checkGeldigAantalSpelers_Geenspelers_retourneertSpelerLijstLeegException() {
	SpelerRepo legeRepo = new SpelerRepo();
	assertThrows(SpelerLijstLeegException.class, () -> {
	    new Spel(new DomeinController(), legeRepo);
	});
    }

    /**
     * Constructor spel met 1 speler moet SpelerLijstTeKleinException gooien
     * 
     * @throws OngeldigeKaraktersException
     * @throws GeenSpeelkansenException
     * @throws SpelerLijstTeGrootException
     * @throws DubbeleSpelerException
     */

    @Test
    public void checkGeldigAantalSpelers_éénSpeler_retourneertSpelerLijstTeKleinException()
	    throws DubbeleSpelerException, SpelerLijstTeGrootException, GeenSpeelkansenException,
	    OngeldigeKaraktersException {

	SpelerRepo éénSpelerRepo = new SpelerRepo();

	éénSpelerRepo.voegSpelerToe(new Speler("Robin", LocalDate.of(1998, 1, 1)));

	assertThrows(SpelerLijstTeKleinException.class, () -> {
	    new Spel(new DomeinController(), éénSpelerRepo);
	});
    }

    /**
     * einde spel moet false retourneren indien er nog stenen in speelpot ziten
     */

    @Test
    public void isEindeSpel_StenenNogInSpeelPot_retourneertFalse() {
	assertFalse(spel.isEindeSpel());
    }

    /**
     * geefwinnaar moet SpelNietAfgelopenException gooien als spel nog bezig is
     */

    @Test
    public void geefWinnaar_SpelNogBezig_retourneertSpelNietAfgelopenException() {
	assertThrows(SpelNietAfgelopenException.class, () -> spel.geefWinnaar());
    }

    /**
     * isAllerEersteBeurt moet true geven bij aanvang van spel
     */

    @Test
    public void isAllerEersteBeurt_eersteBeurt_geeftTrue() {
	assertTrue(spel.isAllerEersteBeurt());
    }

}
