package domein;

import static domein.DomeinRegels.AANTAL_NIEUWE_STENEN_BEURT;
import static domein.DomeinRegels.AANTAL_NIEUWE_STENEN_EERSTE_BEURT_SPEL;
import static domein.DomeinRegels.AANTAL_STENEN_PER_WAARDE;
import static domein.DomeinRegels.SPEELKANSEN_GEWONNEN_ALS_WINNAAR;
import static domein.DomeinRegels.SPEELKANSEN_VERLOREN_PER_START_SPEL;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import exceptions.GeenWinnaarException;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
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
import exceptions.SpelerLijstTeKleinException;

/***
 * DomeinKlasse te instantieren bij het start spelen een spel Zatre
 * 
 * @author Naoufal Thabet
 * @author Tom Coenen
 *
 */
public class Spel {

    private DomeinController dc;
    private SpelerRepo spelerRepo;
    private Map<Speler, Scoreblad> spelerScores;
    private Spelbord spelbord;
    private Speelpot speelpot;
    private Speler spelerAanDeBeurt;
    private int zetVanHuidigeSpeler;
    private static int teller;
    private List<Steen> spelerStenen;
    private List<Speler> winnaars;
    private int[] scoreHuidigeBeurt = new int[4];
    private boolean isAllerEersteBeurt;

    /**
     * Constructor klasse Spel
     * 
     * @param spelers Een repo met spelers voor het aan te maken spel.
     * @throws SpelerLijstTeKleinException Exception die gegooid wordt wanneer men
     *                                     het spel probeert te starten terwijl de
     *                                     actieve spelerslijst nog niet genoeg
     *                                     spelers bevat.
     * @throws SpelerLijstLeegException    Exception die gegooid wordt wanneer de
     *                                     spelerlijst leeg is, maar toch
     *                                     aangesproken wordt.
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
    public Spel(DomeinController dc, SpelerRepo spelers) throws SpelerLijstLeegException, SpelerLijstTeKleinException,
	    OngeldigeKaraktersException, NoSuchPlayerException {

	this.dc = dc;
	spelerRepo = spelers;
	checkGeldigAantalSpelers();
	spelbord = new Spelbord();
	speelpot = new Speelpot();
	verminderSpeelkansen();
	spelerRepo.bepaalVolgorde();
	setSpelerScores(vulSpelerScores());
	teller = 0;
	spelerAanDeBeurt = spelerRepo.getGeselecteerdeSpelers().get(teller);
	vraagNieuweStenen();
	isAllerEersteBeurt = isAllerEersteBeurt();
    }

    /**
     * Voor een spel gestart kan worden wordt er gecontroleerd of er een geldig
     * aantal deelnemers aanwezig is.
     * 
     * @return true indien geldig
     * @throws SpelerLijstLeegException    Exception die gegooid wordt wanneer de
     *                                     spelerlijst leeg is, maar toch
     *                                     aangesproken wordt.
     * @throws SpelerLijstTeKleinException Exception die gegooid wordt wanneer men
     *                                     het spel probeert te starten terwijl de
     *                                     actieve spelerslijst nog niet genoeg
     *                                     spelers bevat.
     */
    private boolean checkGeldigAantalSpelers() throws SpelerLijstLeegException, SpelerLijstTeKleinException {
	if (spelerRepo.getGeselecteerdeSpelers() == null || spelerRepo.getGeselecteerdeSpelers().isEmpty()) {
	    throw new SpelerLijstLeegException("FoutmeldingSpelerlijstLeeg");
	}
	if (spelerRepo.getGeselecteerdeSpelers().size() < 2) {
	    throw new SpelerLijstTeKleinException("FoutmeldingSpelerlijstTeKlein");
	}
	return true;
    }

    /**
     * Hulpmethode vult Map met Speler objecten uit lijst geselecteerde spelers met
     * een nieuwe Scoreblad
     * 
     * @throws SpelerLijstLeegException    Exception die gegooid wordt wanneer de
     *                                     spelerlijst leeg is, maar toch
     *                                     aangesproken wordt.
     * @throws SpelerLijstTeKleinException Exception die gegooid wordt wanneer men
     *                                     het spel probeert te starten terwijl de
     *                                     actieve spelerslijst nog niet genoeg
     *                                     spelers bevat.
     */
    private Map<Speler, Scoreblad> vulSpelerScores() throws SpelerLijstLeegException, SpelerLijstTeKleinException {
	Map<Speler, Scoreblad> lijst = new HashMap<Speler, Scoreblad>();
	checkGeldigAantalSpelers();

	for (Speler speler : spelerRepo.getGeselecteerdeSpelers()) {
	    lijst.put(speler, new Scoreblad());
	}

	return lijst;
    }

    private void setSpelerScores(Map<Speler, Scoreblad> spelerScores) {
	if (spelerScores == null || spelerScores.isEmpty()) {
	    throw new IllegalArgumentException("foutmeldingSpelerScoresLegeLijst");
	}
	this.spelerScores = spelerScores;
    }

    /**
     * Geeft de huidige speler aan de beurt
     * 
     * @return String met de naam van de huidige speler.
     */
    public String geefSpelerAanDeBeurt() {
	return spelerAanDeBeurt.getGebruikersnaam();
    }

    /**
     * Methode die kan gebruikt worden om na te gaan of het spel afgelopen is
     * 
     * @return geeft true wanneer het spel afgelopen is.
     */
    public boolean isEindeSpel() {
	return speelpot.getStenen().isEmpty() && spelerStenen.isEmpty();
    }

    /**
     * Methode geeft alle scoreborden van de actieve spelers weer.
     * 
     * @return ArrayList van String: scoreborden van alle spelers onder elkaar.
     * @throws SpelerLijstLeegException Exception die gegooid wordt wanneer de
     *                                  spelerlijst leeg is, maar toch aangesproken
     *                                  wordt.
     */
    public List<String> geefScorebladenAlleSpelers() throws SpelerLijstLeegException {
	List<String> out = new ArrayList<>();

	spelerRepo.getGeselecteerdeSpelers().forEach(speler -> out.add(String.format("%s%n%s",
		speler.getGebruikersnaam(), spelerScores.get(speler).geefOverzichtEindScore())));

	return out;
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
	int[] out = new int[spelerScores.size()];
	int counter = 0;

	for (Speler s : spelerRepo.getGeselecteerdeSpelers()) {
	    out[counter++] = spelerScores.get(s).geefEindScore();
	}

	return out;
    }

    /**
     * Methode om de winnaar van een beurt Zatre te achterhalen.
     * 
     * @return gebruikersnaam met hoogste totale score op de scoreblad.
     * @throws GeenWinnaarException        Exception die gegooid wordt wanneer
     *                                     hetspel na afloop geen winnaar kan
     *                                     bepalen.
     * @throws SpelNietAfgelopenException  Exception die gegooid wordt wanneer het
     *                                     spel niet afgelopen is, maar toch
     *                                     methodes aangeroepen worden die pas na
     *                                     afloop kunnen gebruikt worden.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws OngeldigeKaraktersException Exception die gegooid wordt indien een
     *                                     input naar het domein ongeldige karakters
     *                                     bevat, ter preventie van MYSQL en/of
     *                                     domeinissues. Ongeldige karakters: \ ' "
     *                                     # & %
     * 
     *
     */
    public List<Speler> geefWinnaar() throws SpelNietAfgelopenException, GeenWinnaarException,
	    OngeldigeKaraktersException, NoSuchPlayerException {
	stelWinnaarIn();
	return winnaars;
    }

    /**
     * Hulpmethode om winnaar van een spel Zatre te bepalen
     * 
     * @return Speler object met hoogste totale score op de scoreblad.
     * @throws GeenWinnaarException        Exception die gegooid wordt wanneer
     *                                     hetspel na afloop geen winnaar kan
     *                                     bepalen.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws OngeldigeKaraktersException Exception die gegooid wordt indien een
     *                                     input naar het domein ongeldige karakters
     *                                     bevat, ter preventie van MYSQL en/of
     *                                     domeinissues. Ongeldige karakters: \ ' "
     *                                     # & %
     * @throws SpelerLijstLeegException    Exception die gegooid wordt wanneer de
     *                                     spelerlijst leeg is, maar toch
     *                                     aangesproken wordt.
     */
    private void bepaalWinnaar() throws GeenWinnaarException, OngeldigeKaraktersException, NoSuchPlayerException {

	int scorewinnaar = spelerScores.values().stream().map(Scoreblad::geefEindScore).mapToInt(Integer::intValue)
		.max().getAsInt();
	winnaars = spelerScores.entrySet().stream().filter(entry -> entry.getValue().geefEindScore() == scorewinnaar)
		.map(entry -> entry.getKey()).toList();

	if (Objects.isNull(winnaars))
	    throw new GeenWinnaarException("FoutMeldingSpelWinnaarLeeg");

	for (Speler winnaar : winnaars) {
	    winnaar.setSpeelkansen(winnaar.getSpeelkansen() + SPEELKANSEN_GEWONNEN_ALS_WINNAAR);
	    dc.updateSpeler(winnaar);
	}

    }

    /**
     * Hulpmethode om de winnaar van een spel in te stellen.
     * 
     * @throws SpelNietAfgelopenException  Exception die gegooid wordt wanneer het
     *                                     spel niet afgelopen is, maar toch
     *                                     methodes aangeroepen worden die pas na
     *                                     afloop kunnen gebruikt worden.
     * @throws GeenWinnaarException        Exception die gegooid wordt wanneer het
     *                                     spel na afloop geen winnaar kan bepalen.
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
    private void stelWinnaarIn() throws SpelNietAfgelopenException, GeenWinnaarException, OngeldigeKaraktersException,
	    NoSuchPlayerException {
	if (!isEindeSpel())
	    throw new SpelNietAfgelopenException("FoutMeldingSpelGeenWinnaarEindSpelFalse");
	if (winnaars == null) {
	    bepaalWinnaar();
	}

    }

    /**
     * Hulpmethode om de volgende speler aan de beurt te bepalen.
     * 
     * @return Speler object die volgend aan beurt is.
     * @throws SpelerLijstLeegException Exception die gegooid wordt wanneer de
     *                                  spelerlijst leeg is, maar toch aangesproken
     *                                  wordt.
     */
    private void bepaalVolgendeSpelerAanBeurt() throws SpelerLijstLeegException {
	if (!isEindeSpel() && isEindeBeurt()) {
	    spelerAanDeBeurt = spelerRepo.getGeselecteerdeSpelers()
		    .get(++teller % spelerRepo.getGeselecteerdeSpelers().size());
	}
    }

    /**
     * Hulpmethode die het aantal speelkansen van de spelers vermindert bij aanvang
     * van het spel.
     * 
     * @throws SpelerLijstLeegException    Exception die gegooid wordt wanneer de
     *                                     spelerlijst leeg is, maar toch
     *                                     aangesproken wordt.
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
    private void verminderSpeelkansen()
	    throws SpelerLijstLeegException, OngeldigeKaraktersException, NoSuchPlayerException {
	for (Speler s : spelerRepo.getGeselecteerdeSpelers()) {
	    s.setSpeelkansen(s.getSpeelkansen() - SPEELKANSEN_VERLOREN_PER_START_SPEL);
	    dc.updateSpeler(s);
	}
    }

    // Methoden UC4

    /**
     * Bepaalt of de beurt van de huidige speler voorbij is (Alle stenen gelegd)
     * 
     * @return true indien einde beurt, anders false.
     */
    public boolean isEindeBeurt() {
	return spelerStenen.isEmpty();
    }

    /**
     * Hulpmethode die bepaalt of dit de allereerste beurt is van het spel.
     * 
     * @return True wanneer er nog geen steen is geplaatste op het spelbord en
     *         wanneer speelpot nog volledig is.
     */
    public boolean isAllerEersteBeurt() {
	int stenenInSpel = speelpot.getStenen().size();
	if (spelerStenen != null) {
	    stenenInSpel += spelerStenen.size();
	}

	return spelbord.isBordLeeg() && stenenInSpel == Arrays.stream(AANTAL_STENEN_PER_WAARDE).sum();
    }

    /**
     * Methode die aangeroepen wordt aan het begin van een spelerbeurt.
     */
    private void vraagNieuweStenen() {
	// Eerste zet krijgt een negatieve teller. Laat de speler toe al zijn stenen te
	// plaatsen in de eerste beurt.
	spelerScores.get(spelerAanDeBeurt).firePropertyChange();
	if (isAllerEersteBeurt()) {
	    setSpelerStenen(speelpot.geefNieuweStenen(AANTAL_NIEUWE_STENEN_EERSTE_BEURT_SPEL));
	    zetVanHuidigeSpeler = -1;
	} else {
	    setSpelerStenen(speelpot.geefNieuweStenen(AANTAL_NIEUWE_STENEN_BEURT));
	    zetVanHuidigeSpeler = 1;
	}
    }

    /**
     * Leg de meegegeven steen op de gevraagde coordinaten.
     * 
     * @param steen De te leggen steen.
     * @param posX  x-coordinaat
     * @param posY  y-coordinaat
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
    public void legSteen(int steen, int posX, int posY)
	    throws SpelSpelerHeeftDezeSteenNietException, SpelbordOngeldigeZetNietInMiddenException,
	    SpelbordOngeldigeZetVakNietLeegException, SpelbordOngeldigeZetGeblokkeerdVakException,
	    SpelbordOngeldigeZetNietAanpalendException, SpelbordOngeldigeZetGrijsVakException,
	    SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException, SpelbordOngeldigeZetSomHogerDanTwaalfException {
	Steen teLeggenSteen = new Steen(steen);

	// Check steen aanwezig in hand Speler
	if (spelerStenen.indexOf(teLeggenSteen) == -1)
	    throw new SpelSpelerHeeftDezeSteenNietException("FoutmeldingSteenNietGevondenInSpelerStenenSpel");

	spelbord.legSteen(teLeggenSteen, posX, posY, zetVanHuidigeSpeler);
	spelerStenen.remove(teLeggenSteen);
	zetVanHuidigeSpeler++;
	slaScoreEindeZetOp(steen, posX, posY);

    }

    /**
     * Methode die aangeroepen wordt aan het einde van een beurt. Steekt de lijst
     * spelerStenen terug in de speelpot, slaat score op, bepaalt volgende speler en
     * genereert stene voor die speler.
     * 
     * @throws SpelerLijstLeegException
     */
    public void legOverschotStenenTerug() throws SpelerLijstLeegException {
	speelpot.legStenenTerug(spelerStenen);
	spelerStenen.clear();
	spelerScores.get(spelerAanDeBeurt).eindeBeurt();
	bepaalVolgendeSpelerAanBeurt();
	speelpot.schudStenen();
	vraagNieuweStenen();
	isAllerEersteBeurt = isAllerEersteBeurt();
    }

    /**
     * Zet de meegegeven lijst van stenen als spelerstenen in.
     * 
     * @param spelerStenen Lijst van stenen voor de speler.
     */
    private void setSpelerStenen(List<Steen> spelerStenen) {
	if (spelerStenen == null || spelerStenen.isEmpty()) {
	    throw new IllegalArgumentException("foutmeldingSteenLijstLeeg");
	}
	this.spelerStenen = spelerStenen;
    }

    /**
     * Wordt aangeroepen aan het einde van een beurt, update de score van de huidige
     * speler.
     */
    private void slaScoreEindeZetOp(int steen, int posX, int posY) {
	Arrays.fill(scoreHuidigeBeurt, 0);

	// Bereken score van deze zet
	int[] scoreZet = spelbord.berekenSomVanZet(steen, posX, posY);

	// Check score van 10, 11 of 12 op x-as [0]
	// Check score van 10, 11 of 12 op y-as [1]

	for (int i = 0; i <= 1; i++) {
	    switch (scoreZet[i]) {
	    case 10 -> scoreHuidigeBeurt[1]++;
	    case 11 -> scoreHuidigeBeurt[2]++;
	    case 12 -> scoreHuidigeBeurt[3]++;
	    }
	}

	// Check of x2 voor grijs vak of middelste vak.
	scoreHuidigeBeurt[0] = scoreZet[2];

	// Schrijf score weg van huidige zet.

	if (Arrays.stream(Arrays.copyOfRange(scoreHuidigeBeurt, 1, 4)).filter(x -> x > 0).count() > 0) {
	    if (isAllerEersteBeurt) {
		spelerScores.get(spelerAanDeBeurt).voegScoreBladEntryAanScoreblad(true, scoreHuidigeBeurt[1],
			scoreHuidigeBeurt[2], scoreHuidigeBeurt[3]);
	    } else {
		spelerScores.get(spelerAanDeBeurt).voegScoreBladEntryAanScoreblad(scoreHuidigeBeurt[0] == 1,
			scoreHuidigeBeurt[1], scoreHuidigeBeurt[2], scoreHuidigeBeurt[3]);
	    }

	}

    }

    /**
     * Geeft de huidige stenen van de speler terug.
     * 
     * @return array van de waarden van de huidige stenen van de speler.
     */
    public int[] geefSpelerStenen() {
	return spelerStenen.stream().mapToInt(steen -> steen.getWaarde()).toArray();
    }

    /**
     * Registreert een meegegeven object als listener van het spelbord.
     * 
     * @param listener De listener voor het spelbord.
     */
    public void registreerListenerSpelbord(@SuppressWarnings("exports") PropertyChangeListener listener) {
	spelbord.addListener(listener);
    }

    /**
     * Registreert een meegegeven object als listener van het scoreblad van de
     * meegegeven speler..
     * 
     * @param listener De listener voor het scoreblad.
     * @param speler   De speler waartoe het scoreblad behoort. niet in de
     *                 repository lijst voorkomt.
     */
    public void registreerListenerScoreblad(@SuppressWarnings("exports") PropertyChangeListener listener,
	    Speler speler) {
	spelerScores.get(speler).addListener(listener);
    }

    /**
     * De-registreert een meegegeven object als listener van het spelbord.
     * 
     * @param listener De te verwijderen listener van het spelbord.
     */
    public void verwijderListenerSpelbord(@SuppressWarnings("exports") PropertyChangeListener listener) {
	spelbord.removeListener(listener);
    }

    /**
     * De-registreert een meegegeven object als listener van het scoreblad.
     * 
     * @param listener De te verwijderen listener van het spelbord.
     * @param speler   De speler waartoe het scoreblad behoort.
     * 
     */
    public void verwijderListenerScoreblad(@SuppressWarnings("exports") PropertyChangeListener listener,
	    Speler speler) {
	spelerScores.get(speler).removeListener(listener);
    }

    /**
     * Methode die ui kan aanroepen om voor alle ingenomen vakken een update van de
     * listener aan te vragen. Stuurt ook het scorebord van de speler aan de beurt
     * opnieuw terug.
     */
    public void herbouwSpelbord() {
	spelbord.herbouwSpelbord();
	spelerScores.get(spelerAanDeBeurt).firePropertyChange();
    }

    /**
     * Geeft het aantal stenen die nog in de pot zitten.
     * 
     * @return int: aantal stenen in de pot.
     */
    public int geefAantalStenenResterend() {
	return speelpot.getStenen().size();
    }

    /**
     * Stopt het spel voortijdig: verwijdert alle stenen uit de pot en hand van de
     * huidige speler.
     */
    public void stopSpel() {
	speelpot.stopSpel();
	spelerStenen.clear();
    }

}
