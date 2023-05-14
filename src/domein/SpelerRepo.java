package domein;

import static domein.DomeinRegels.MAX_SPELERS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import exceptions.DubbeleSpelerException;
import exceptions.GeenSpeelkansenException;
import exceptions.SpelerLijstLeegException;
import exceptions.SpelerLijstTeGrootException;
import exceptions.SpelerNietInLijstException;

/**
 * Repository die de lijst van huidige spelers bijhoudt.
 * 
 * @author tomco
 *
 */
public class SpelerRepo {

    private List<Speler> spelerslijst;

    /**
     * Constructor, initialiseert de lijst.
     */
    public SpelerRepo() {
	spelerslijst = new ArrayList<>();
    }

    /**
     * Geeft een lijst van de huidige spelers terug
     * 
     * @return List<Spelers>
     * @throws SpelerLijstLeegException Exception die gegooid wordt wanneer de
     *                                  spelerlijst leeg is, maar toch aangesproken
     *                                  wordt.
     */
    public List<Speler> getGeselecteerdeSpelers() throws SpelerLijstLeegException {
	if (spelerslijst.isEmpty()) {
	    throw new SpelerLijstLeegException();
	}
	return spelerslijst;
    }

    /**
     * Voeg een speler toe aan de lijst van actieve spelers.
     * 
     * @param gebruikersnaam naam van de toe te voegen speler
     * @param geboortejaar   geboortejaar van de toe te voegen speler
     * @throws DubbeleSpelerException      Wordt gegooid indien de speler reeds in
     *                                     de actieve lijst aanwezig is
     * @throws SpelerLijstTeGrootException Wordt gegooid indien het aantal spelers
     *                                     in de actieve lijst reeds het maximum
     *                                     bereikt heeft.
     * @throws GeenSpeelkansenException    Exception die gegooid wordt wanneer de
     *                                     speler geen speelkansen over heeft, en
     *                                     dus niet kan gekozen worden.
     */
    public void voegSpelerToe(Speler speler)
	    throws DubbeleSpelerException, SpelerLijstTeGrootException, GeenSpeelkansenException {
	if (spelerslijst.contains(speler)) {
	    throw new DubbeleSpelerException();
	}
	if (spelerslijst.size() == MAX_SPELERS) {
	    throw new SpelerLijstTeGrootException();
	}
	if (speler.getSpeelkansen() == 0) {
	    throw new GeenSpeelkansenException();
	}
	spelerslijst.add(speler);
    }

    /**
     * Verwijder een speler van de lijst van actieve spelers.
     * 
     * @param gebruikersnaam naam van de toe te voegen speler
     * @param geboortejaar   geboortejaar van de toe te voegen speler
     * @throws SpelerNietInLijstException Wordt gegooid indien de te verwijderen
     *                                    speler niet voorkomt in de lijst van
     *                                    actieve spelers.
     * @throws SpelerLijstLeegException   Exception die gegooid wordt wanneer de
     *                                    spelerlijst leeg is, maar toch
     *                                    aangesproken wordt.
     */
    public void verwijderSpeler(Speler speler) throws SpelerNietInLijstException, SpelerLijstLeegException {
	if (spelerslijst.isEmpty()) {
	    throw new SpelerLijstLeegException();
	}
	if (!spelerslijst.contains(speler)) {
	    throw new SpelerNietInLijstException();
	}
	spelerslijst.remove(speler);
    }

    // UC3

    /**
     * Methode die de lijst van Speler objecten willekeurig herschikt
     */
    public void bepaalVolgorde() {
	Collections.shuffle(spelerslijst);
    }

    /**
     * Geeft een Speler object terug indien deze speler in de lijst van huidige
     * speler zit. Opgelet: Indien meerdere keren
     * 
     * @param naam         Naam van de gezochte speler
     * @param geboortejaar Geboortejaar van de gezochte speler
     * @return Speler object
     * @throws SpelerNietInLijstException
     * 
     */
    public Speler zoekSpelerInLijst(String naam, LocalDate geboortejaar) throws SpelerNietInLijstException {
	return spelerslijst.stream()
		.filter(s -> s.getGeboortejaar().equals(geboortejaar) && s.getGebruikersnaam().equals(naam)).findFirst()
		.orElseThrow(() -> new SpelerNietInLijstException("FoutmeldingSpelerNietActief"));
    }
}
