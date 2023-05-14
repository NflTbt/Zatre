package domein;

import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

import domein.DomeinRegels.VakKleur;
import exceptions.DubbeleSpelerException;
import exceptions.GeenSpeelkansenException;
import exceptions.GeenWinnaarException;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
import exceptions.PlayerExistsException;
import exceptions.SpelNietAfgelopenException;
import exceptions.SpelSpelerHeeftDezeSteenNietException;
import exceptions.SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException;
import exceptions.SpelbordOngeldigeZetGeblokkeerdVakException;
import exceptions.SpelbordOngeldigeZetGrijsVakException;
import exceptions.SpelbordOngeldigeZetNietAanpalendException;
import exceptions.SpelbordOngeldigeZetNietInMiddenException;
import exceptions.SpelbordOngeldigeZetSomHogerDanTwaalfException;
import exceptions.SpelbordOngeldigeZetVakNietLeegException;
import exceptions.SpelerLijstLeegException;
import exceptions.SpelerLijstTeGrootException;
import exceptions.SpelerLijstTeKleinException;
import exceptions.SpelerNietInLijstException;
import persistentie.PersistentieController;

/**
 * Controller van de domeinklasse
 * 
 * @author Valentijn De Borggrave
 * @author Tom Coenen
 *
 */
public class DomeinController {

    private final PersistentieController persistentieController;
    private Speler nieuwGeregistreerdeSpeler;
    private SpelerRepo geselecteerdeSpelers;
    private Spel spel;

    // UC1 methodes:

    /**
     * Constructor, maakt instanties van persistentiecontroller en speler repository
     * aan.
     */
    public DomeinController() {
	persistentieController = new PersistentieController();
	geselecteerdeSpelers = new SpelerRepo();
    }

    /**
     * Registreert een nieuwe speler, slaat op als nieuwGeregistreerdeSpeler en
     * schrijft naar persistentielaag.
     * 
     * @param gebruikersnaam De naam van de te registreren speler
     * @param geboortejaar   Geboortejaar van de geregistreerde speler [YYYY]
     * @throws PlayerExistsException       Wordt gegeven indien de speler reeds in
     *                                     de database aanwezig is.
     * @throws OngeldigeKaraktersException Wordt gegooid indien de gegeven
     *                                     gebruikersnaam 1 van de geblokkeerde
     *                                     karakters uit de domeinregels bevat.
     */
    public void registreer(String gebruikersnaam, LocalDate geboortejaar)
	    throws PlayerExistsException, OngeldigeKaraktersException {
	Speler nieuweSpeler = new Speler(gebruikersnaam, geboortejaar);
	persistentieController.voegSpelerToe(nieuweSpeler);
	setNieuwGeregistreerdeSpeler(nieuweSpeler);
    }

    /**
     * 
     * @return Geeft de overblijvende speelkansen van de huidige speler
     */
    public int geefSpeelkansen() {
	return nieuwGeregistreerdeSpeler.getSpeelkansen();
    }

    /**
     * 
     * @return Geeft de gebruikersnaam van de huidige speler
     */
    public String geefGebruikersnaam() {
	return nieuwGeregistreerdeSpeler.getGebruikersnaam();
    }

    /**
     * Slaat de nieuw geregistreerde speler op als actieve speler in de domeinklasse
     * 
     * @param nieuweSpeler De te registreren Speler
     */
    private void setNieuwGeregistreerdeSpeler(Speler nieuweSpeler) {
	nieuwGeregistreerdeSpeler = nieuweSpeler;
    }

    // UC 2 Methodes:

    /**
     * Voeg een speler toe aan de lijst van actieve spelers
     * 
     * @param gebruikersnaam Gebruikersnaam van de toe te voegen speler
     * @param geboortejaar   Geboortejaar van de toe te voegen speler.
     * @throws SpelerLijstTeGrootException Wordt gegooid indien het aantal spelers
     *                                     in de actieve lijst reeds het maximum
     *                                     bereikt heeft.
     * @throws DubbeleSpelerException      Wordt gegooid indien de speler reeds in
     *                                     de actieve lijst aanwezig is
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws GeenSpeelkansenException    Exception die gegooid wordt wanneer de
     *                                     speler geen speelkansen over heeft, en
     *                                     dus niet kan gekozen worden.
     */
    public void voegSpelerToe(String gebruikersnaam, LocalDate geboortejaar) throws OngeldigeKaraktersException,
	    DubbeleSpelerException, SpelerLijstTeGrootException, NoSuchPlayerException, GeenSpeelkansenException {

	geselecteerdeSpelers.voegSpelerToe(persistentieController.zoekSpeler(gebruikersnaam, geboortejaar));
    }

    /**
     * Verwijder een speler van de lijst van actieve spelers
     * 
     * @param gebruikersnaam Gebruikersnaam van de te verwijderen speler
     * @param geboortejaar   Geboortejaar van de te verwijderen speler.
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws SpelerNietInLijstException  Wordt gegooid indien de te verwijderen
     *                                     speler niet voorkomt in de lijst van
     *                                     actieve spelers.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws SpelerLijstLeegException    Exception die gegooid wordt wanneer de
     *                                     spelerlijst leeg is, maar toch
     *                                     aangesproken wordt.
     */
    public void verwijderSpeler(String gebruikersnaam, LocalDate geboortejaar)
	    throws SpelerNietInLijstException, OngeldigeKaraktersException, SpelerLijstLeegException {
	geselecteerdeSpelers.verwijderSpeler(new Speler(gebruikersnaam, geboortejaar));
    }

    /**
     * Geeft een overzicht terug van alle momenteel geselecteerde spelers:
     * Gebruikersnaam, geboortejaar en aantal resterende speelkansen.
     * 
     * @return Een string met alle geselecteerde spelers. Elke speler is een item in
     *         de array, per speler worden gebruikersnaam, geboortejaar en aantal
     *         resterende speelkansen opgesomd, gescheiden door '#'.
     * @throws SpelerLijstLeegException Exception die gegooid wordt wanneer de
     *                                  spelerlijst leeg is, maar toch aangesproken
     *                                  wordt.
     */
    public String[] geefOverzichtSpelers() throws SpelerLijstLeegException {
	List<Speler> lijst = geselecteerdeSpelers.getGeselecteerdeSpelers();

	return lijst.stream().map(speler -> String.format("%s#%d#%d", speler.getGebruikersnaam(),
		speler.getGeboortejaar().getYear(), speler.getSpeelkansen())).toArray(String[]::new);

    }

    // UC 3 Methodes:

    /**
     * Methode aan te roepen bij opstarten van het spel. Initialiseert de
     * achterliggende domeinklassen.
     * 
     * @throws SpelerLijstTeKleinException Exception die gegooid wordt wanneer men
     *                                     het spel probeert te starten terwijl de
     *                                     actieve spelerslijst nog niet genoeg
     *                                     spelers bevat.
     * @throws SpelerLijstLeegException    Exception die gegooid wordt wanneer de
     *                                     spelerlijst leeg is, maar toch
     *                                     aangesproken wordt.
     * @throws GeenSpeelkansenException    Exception die gegooid wordt wanneer de
     *                                     speler geen speelkansen over heeft, en
     *                                     dus niet kan gekozen worden.
     * @throws SpelerLijstTeGrootException Exception die gegooid wordt wanneer de
     *                                     SpelerRepository reeds het maximum aantal
     *                                     spelers bevat, en er nog een speler wordt
     *                                     toegevoegd.
     * @throws DubbeleSpelerException      Exception die gegooid wordt wanneer men
     *                                     probeert een speler aan de
     *                                     SpelerRepository toe te voegen die reeds
     *                                     in de lijst aanwezig is.
     * @throws OngeldigeKaraktersException Exception die gegooid wordt indien een
     *                                     input naar het domein ongeldige karakters
     *                                     bevat, ter preventie van MYSQL en/of
     *                                     domeinissues. Ongeldige karakters: \ ' "
     *                                     # & %
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     */
    public void startSpel() throws SpelerLijstLeegException, SpelerLijstTeKleinException, DubbeleSpelerException,
	    SpelerLijstTeGrootException, GeenSpeelkansenException, OngeldigeKaraktersException, NoSuchPlayerException {

	spel = new Spel(this, geselecteerdeSpelers);
    }

    /**
     * Geeft de huidige speler aan de beurt
     * 
     * @return speler aan de beurt
     */
    public String geefSpelerAanDeBeurt() {
	return spel.geefSpelerAanDeBeurt();
    }

    /**
     * Methode die toelaat te chekcen of het spel afgelopen is.
     * 
     * @return true indien afgelopen, false indien nog bezig.
     */
    public boolean isEindeSpel() {
	return spel.isEindeSpel();
    }

    /**
     * Geeft een String overzicht van alle scorebladen van alle spelers
     * 
     * @return Overzicht van alle scorebladen. Zie domeinklasse scoreblad voor
     *         layout.
     * @throws SpelerLijstLeegException Exception die gegooid wordt wanneer de
     *                                  spelerlijst leeg is, maar toch aangesproken
     *                                  wordt.
     */
    public List<String> geefScorebladenAlleSpelers() throws SpelerLijstLeegException {
	return spel.geefScorebladenAlleSpelers();
    }

    /**
     * Geeft een array van int-waarden terug met de eindscore van elke speler, aan
     * te roepen na einde spel.
     * 
     * @return int array met eindscore per speler
     * @throws SpelerLijstLeegException Exception die gegooid wordt wanneer de
     *                                  spelerlijst leeg is, maar toch aangesproken
     *                                  wordt.
     */
    public int[] geefEindScoresAlleSpelers() throws SpelerLijstLeegException {
	return spel.geefEindScoresAlleSpelers();
    }

    /**
     * Indien spel is afgelopen, geeft een String met de naam van de winnaar en zijn
     * resterende speelkansen, gescheiden met #.
     * 
     * @return De winnaar
     * @throws GeenWinnaarException        Exception die gegooid wordt wanneer het
     *                                     spel na afloop geen winnaar kan bepalen.
     * @throws SpelNietAfgelopenException  Exception die gegooid wordt wanneer het
     *                                     spel niet afgelopen is, maar toch
     *                                     methodes aangeroepen worden die pas na
     *                                     afloop kunnen gebruikt worden.
     * @throws NoSuchPlayerException
     * @throws OngeldigeKaraktersException
     */
    public String geefWinnaar() throws SpelNietAfgelopenException, GeenWinnaarException, OngeldigeKaraktersException,
	    NoSuchPlayerException {
	String out = "";
	for (Speler winnaar : spel.geefWinnaar()) {
	    out += String.format("%s#%s\n", winnaar.getGebruikersnaam(), winnaar.getSpeelkansen());
	}
	return out;
    }

    /**
     * Update speelkansen van de speler
     * 
     * @param speler de up te daten speler
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws OngeldigeKaraktersException Exception die gegooid wordt indien een
     *                                     input naar het domein ongeldige karakters
     *                                     bevat, ter preventie van MYSQL en/of
     *                                     domeinissues. Ongeldige karakters: \ ' "
     *                                     # & %
     */
    public void updateSpeler(Speler speler) throws OngeldigeKaraktersException, NoSuchPlayerException {
	persistentieController.updateSpeler(speler);
    }

    // UC 4 Methodes:

    /**
     * Geeft een overzicht van het spelbord met vakkleuren terug
     * 
     * @return VakKleur[][]
     */
    public VakKleur[][] geefOverzichtSpelbord() {
	return DomeinRegels.BORDPATROON;
    }

    /**
     * Leg een steen met de meegegeven waarde op deze positie. Huidige speler moet
     * een steen met deze waarde in zijn bezit hebben.
     * 
     * @param waarde Waarde van de te leggen steen
     * @param posX   x-coordinaat van de steen
     * @param posY   y-coordinaat van de steen
     * @throws SpelSpelerHeeftDezeSteenNietException                Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              Speler een steen
     *                                                              probeert te
     *                                                              leggen die niet
     *                                                              in zijn lijst
     *                                                              zit.
     * @throws SpelbordOngeldigeZetNietInMiddenException            Exception die
     *                                                              gegooid wordt
     *                                                              wanneer de
     *                                                              eerste steen op
     *                                                              het bord niet in
     *                                                              het midden
     *                                                              geplaatst wordt.
     * @throws SpelbordOngeldigeZetNietAanpalendException           Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler probeert
     *                                                              een steen te
     *                                                              leggen op een
     *                                                              vak niet naast
     *                                                              een andere
     *                                                              steen.
     * @throws SpelbordOngeldigeZetGeblokkeerdVakException          Exception die
     *                                                              gegooid wordt
     *                                                              wanneer er een
     *                                                              poging is om een
     *                                                              steen op een
     *                                                              geblokkeerd vak
     *                                                              te leggen.
     * @throws SpelbordOngeldigeZetVakNietLeegException             Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler een zet
     *                                                              doet op een
     *                                                              reeds ingenomen
     *                                                              vak.
     * @throws SpelbordOngeldigeZetGrijsVakException                Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler een
     *                                                              ongeldige zet
     *                                                              doet op een
     *                                                              grijs vak.
     * @throws SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler een steen
     *                                                              legt die enkel
     *                                                              grenst aan zijn
     *                                                              eigen stenen van
     *                                                              dezelfde beurt,
     *                                                              en niet aan de
     *                                                              stenen van een
     *                                                              andere speler.
     * @throws SpelbordOngeldigeZetSomHogerDanTwaalfException       Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een zet
     *                                                              een score zou
     *                                                              opleveren die
     *                                                              hoger is dan 12.
     */
    public void legSteen(int waarde, int posX, int posY)
	    throws SpelSpelerHeeftDezeSteenNietException, SpelbordOngeldigeZetNietInMiddenException,
	    SpelbordOngeldigeZetVakNietLeegException, SpelbordOngeldigeZetGeblokkeerdVakException,
	    SpelbordOngeldigeZetNietAanpalendException, SpelbordOngeldigeZetGrijsVakException,
	    SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException, SpelbordOngeldigeZetSomHogerDanTwaalfException {
	spel.legSteen(waarde, posX, posY);
    }

    /**
     * Geef de stenen van de huidige speler
     * 
     * @return Array met de stenen van de huidige speler
     */
    public int[] geefStenenSpelerAanDeBeurt() {

	return spel.geefSpelerStenen();
    }

    /**
     * Wordt aangeroepen wanneer de huidige speler aangeeft niet verder te
     * kunnen/willen spelen met zijn huidige stenen.
     * 
     * @throws SpelerLijstLeegException
     * 
     */
    public void legStenenTerugInSpeelpot() throws SpelerLijstLeegException {
	spel.legOverschotStenenTerug();
    }

    /**
     * Registreert een meegegeven object als listener van het spelbord.
     * 
     * @param listener De listener voor het spelbord.
     */
    public void registreerListenerSpelbord(@SuppressWarnings("exports") PropertyChangeListener listener) {
	spel.registreerListenerSpelbord(listener);
    }

    /**
     * De-registreert een meegegeven object als listener van het spelbord.
     * 
     * @param listener De te verwijderen listener van het spelbord.
     */
    public void verwijderListenerSpelbord(@SuppressWarnings("exports") PropertyChangeListener listener) {
	spel.verwijderListenerSpelbord(listener);
    }

    /**
     * Registreert een meegegeven object als listener van het scoreblad van de
     * meegegeven speler..
     * 
     * @param listener De listener voor het scoreblad.
     * @param speler   De speler waartoe het scoreblad behoort.
     * @throws SpelerNietInLijstException Exception die gegooid wordt wanneer men
     *                                    een speler probeert aan te spreken die
     *                                    niet in de repository lijst voorkomt.
     */
    public void registreerListenerScoreblad(@SuppressWarnings("exports") PropertyChangeListener listener,
	    String gebruikersnaam, int geboortejaar) throws SpelerNietInLijstException {
	spel.registreerListenerScoreblad(listener,
		geselecteerdeSpelers.zoekSpelerInLijst(gebruikersnaam, LocalDate.of(geboortejaar, 1, 1)));
    }

    /**
     * De-registreert een meegegeven object als listener van het scoreblad.
     * 
     * @param listener De te verwijderen listener van het spelbord.
     * @param speler   De speler waartoe het scoreblad behoort.
     * @throws SpelerNietInLijstException Exception die gegooid wordt wanneer men
     *                                    een speler probeert aan te spreken die
     *                                    niet in de repository lijst voorkomt.
     */
    public void verwijderListenerScoreblad(@SuppressWarnings("exports") PropertyChangeListener listener,
	    String gebruikersnaam, int geboortejaar) throws SpelerNietInLijstException {
	spel.verwijderListenerScoreblad(listener,
		geselecteerdeSpelers.zoekSpelerInLijst(gebruikersnaam, LocalDate.of(geboortejaar, 1, 1)));
    }

    /**
     * Methode die ui kan aanroepen om voor alle ingenomen vakken een update van de
     * listener aan te vragen.
     */
    public void herbouwSpelbord() {
	spel.herbouwSpelbord();
    }

    /**
     * Geeft het aantal stenen die nog in de pot zitten.
     * 
     * @return int: aantal stenen in de pot.
     */
    public int geefAantalStenenResterend() {
	return spel.geefAantalStenenResterend();
    }

    /**
     * Stopt het spel voortijdig: verwijdert alle stenen uit de pot en hand van de
     * huidige speler.
     */
    public void stopSpel() {
	spel.stopSpel();
    }

}