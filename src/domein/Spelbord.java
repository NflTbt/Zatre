package domein;

import static domein.DomeinRegels.BORDPATROON;
import static domein.DomeinRegels.MAX_SOM_VAN_STENEN_BIJ_EEN_ZET;
import static domein.DomeinRegels.MIDDENPOSITIE_X_SPELBORD;
import static domein.DomeinRegels.MIDDENPOSITIE_Y_SPELBORD;
import static domein.DomeinRegels.SCORES_GRIJS_VAK;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import domein.DomeinRegels.VakKleur;
import exceptions.SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException;
import exceptions.SpelbordOngeldigeZetGeblokkeerdVakException;
import exceptions.SpelbordOngeldigeZetGrijsVakException;
import exceptions.SpelbordOngeldigeZetNietAanpalendException;
import exceptions.SpelbordOngeldigeZetNietInMiddenException;
import exceptions.SpelbordOngeldigeZetSomHogerDanTwaalfException;
import exceptions.SpelbordOngeldigeZetVakNietLeegException;

// UC 3

/**
 * DomeinKlasse te instantieren bij het spelen van Zatre.
 * 
 * @author Naoufal Thabet
 * @author Tom Coenen
 *
 */
public class Spelbord {

    private Spelvak[][] spelbord;
    private PropertyChangeSupport pcs;
    private boolean bordLeeg = true;
    private int eersteZetVanBeurtX;
    private int eersteZetVanBeurtY;

    /**
     * Constructor klasse Spelbord.
     * 
     */
    public Spelbord() {
	setSpelbord(opvullenSpelbord());
	pcs = new PropertyChangeSupport(this);
    }

    /**
     * Geeft het spelbord
     * 
     * @return spelvakken spelvakken vormen samen een spelbord.
     */
    public Spelvak[][] getSpelbord() {
	return spelbord;
    }

    /**
     * stelt de spelvakken in
     * 
     * @param spelbord 2D array van spelvak objecten die een spelbord maken
     */
    private void setSpelbord(Spelvak[][] spelbord) {
	if (spelbord == null)
	    throw new IllegalArgumentException("FoutMeldingOngeldigSpelbord");
	this.spelbord = spelbord;
    }

    /**
     * Methode die toelaat te checken of het spelbord leeg is.
     * 
     * @return true indien leeg, anders false.
     */
    public boolean isBordLeeg() {
	return bordLeeg;
    }

    /**
     * Hulpmethod , helpt spelbord opvullen met spelvakken in een geldige staat.
     * 
     * @return Spelvak[][] spelvakken vormen samen een spelbord
     */
    private Spelvak[][] opvullenSpelbord() {
	Spelvak[][] spelbord = new Spelvak[BORDPATROON.length][];

	for (int rij = 0; rij < BORDPATROON.length; rij++) {
	    spelbord[rij] = new Spelvak[BORDPATROON[rij].length];
	    for (int vak = 0; vak < BORDPATROON[rij].length; vak++) {
		spelbord[rij][vak] = new Spelvak(BORDPATROON[rij][vak]);
	    }
	}
	return spelbord;
    }

    // UC4

    /**
     * Leg een steen op de meegegeven coordinaten
     * 
     * @param steen De te leggen steen.
     * @param x     x-coordinaat
     * @param y     y-coordinaat
     * @throws SpelbordOngeldigeZetVakNietLeegException             Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler een zet
     *                                                              doet op een
     *                                                              reeds ingenomen
     *                                                              vak.
     * @throws SpelbordOngeldigeZetGeblokkeerdVakException          Exception die
     *                                                              gegooid wordt
     *                                                              wanneer er een
     *                                                              poging is om een
     *                                                              steen op een
     *                                                              geblokkeerd vak
     *                                                              te leggen.
     * @throws SpelbordOngeldigeZetNietAanpalendException           Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler probeert
     *                                                              een steen te
     *                                                              leggen op een
     *                                                              vak niet naast
     *                                                              een andere
     *                                                              steen.
     * @throws SpelbordOngeldigeZetGrijsVakException                Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler een
     *                                                              ongeldige zet
     *                                                              doet op een
     *                                                              grijs vak.
     * @throws SpelbordOngeldigeZetNietInMiddenException            Exception die
     *                                                              gegooid wordt
     *                                                              wanneer de
     *                                                              eerste steen op
     *                                                              het bord niet in
     *                                                              het midden
     *                                                              geplaatst wordt.
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
     * @throws SpelbordOngeldigeZetSomHogerDanTwaalfException
     */
    public void legSteen(Steen steen, int x, int y, int zet) throws SpelbordOngeldigeZetVakNietLeegException,
	    SpelbordOngeldigeZetGeblokkeerdVakException, SpelbordOngeldigeZetNietAanpalendException,
	    SpelbordOngeldigeZetGrijsVakException, SpelbordOngeldigeZetNietInMiddenException,
	    SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException, SpelbordOngeldigeZetSomHogerDanTwaalfException {
	checkGeldig(steen.getWaarde(), x, y, zet);
	spelbord[x][y].setSteen(steen);
	if (bordLeeg) {
	    bordLeeg = false;
	}

	// fire propertychange
	List<Integer> out = new ArrayList<>();
	out.add(x);
	out.add(y);
	out.add(steen.getWaarde());
	PropertyChangeEvent evt = new PropertyChangeEvent(this, this.getClass().getSimpleName(), null, out);
	pcs.firePropertyChange(evt);
    }

    /**
     * Geef de steen op dit coordinaat, of null indien leeg.
     * 
     * @param x x-coordinaat
     * @param y y-coordinaat
     * @return Gevonden steen of null
     */
    public Steen geefSteen(int x, int y) {
	if ((this.spelbord[x][y]).getSteen() != null)
	    return this.spelbord[x][y].getSteen();
	return null;
    }

    /**
     * Geef de waarde van de steen op dit coordinaat, of 0 indien leeg.
     * 
     * @param x x-coordinaat
     * @param y y-coordinaat
     * @return Waarde van de gevonden steen of 0
     */
    public int geefWaarde(int x, int y) {
	if (this.geefSteen(x, y) != null)
	    return this.geefSteen(x, y).getWaarde();
	return 0;
    }

    /**
     * Check of een poging tot zetten steen geldig is.
     * 
     * @param steen waarde steen
     * @param x     x-coordinaat
     * @param y     y-coordinaat
     * @throws SpelbordOngeldigeZetVakNietLeegException             Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler een zet
     *                                                              doet op een
     *                                                              reeds ingenomen
     *                                                              vak.
     * @throws SpelbordOngeldigeZetGeblokkeerdVakException          Exception die
     *                                                              gegooid wordt
     *                                                              wanneer er een
     *                                                              poging is om een
     *                                                              steen op een
     *                                                              geblokkeerd vak
     *                                                              te leggen.
     * @throws SpelbordOngeldigeZetNietAanpalendException           Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler probeert
     *                                                              een steen te
     *                                                              leggen op een
     *                                                              vak niet naast
     *                                                              een andere
     *                                                              steen.
     * @throws SpelbordOngeldigeZetGrijsVakException                Exception die
     *                                                              gegooid wordt
     *                                                              wanneer een
     *                                                              speler een
     *                                                              ongeldige zet
     *                                                              doet op een
     *                                                              grijs vak.
     * @throws SpelbordOngeldigeZetNietInMiddenException            Exception die
     *                                                              gegooid wordt
     *                                                              wanneer de
     *                                                              eerste steen op
     *                                                              het bord niet in
     *                                                              het midden
     *                                                              geplaatst wordt.
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
     * @throws SpelbordOngeldigeZetSomHogerDanTwaalfException
     */
    private void checkGeldig(int steen, int x, int y, int zet) throws SpelbordOngeldigeZetVakNietLeegException,
	    SpelbordOngeldigeZetGeblokkeerdVakException, SpelbordOngeldigeZetNietAanpalendException,
	    SpelbordOngeldigeZetGrijsVakException, SpelbordOngeldigeZetNietInMiddenException,
	    SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException, SpelbordOngeldigeZetSomHogerDanTwaalfException {
	// Check eerste beurt correct in midden
	if (bordLeeg && !isMiddelsteVak(x, y))
	    throw new SpelbordOngeldigeZetNietInMiddenException("FoutmeldingOngeldigAllerEersteBeurtSpel");

	// Check dat vak leeg is
	if (!isVakLeeg(x, y))
	    throw new SpelbordOngeldigeZetVakNietLeegException("FoutmeldingSpelvakNietLeegSpelbord");

	// Check dat vak geblokkeerd is
	else if (isGeblokkeerd(x, y))
	    throw new SpelbordOngeldigeZetGeblokkeerdVakException("FoutmeldingSpelvakNietGeldigSpelbord");

	// Check dat locatie niet buiten spelbord valt in y-as
	else if (isOutOfBoundsYAs(y))
	    throw new IllegalArgumentException("FoutmeldingOngeldigRijSpelbord");

	// Check dat locatie niet buiten spelbord valt in x-as
	else if (isOutOfBoundsXAs(x))
	    throw new IllegalArgumentException("FoutmeldingOngeldigKolomSpelbord");

	// Check dat steen buren heeft, behalve indien middenvak
	else if (!checkBurenSteen(x, y) && !isMiddelsteVak(x, y))
	    throw new SpelbordOngeldigeZetNietAanpalendException("FoutmeldingSpelvakNietAanpalendSpelbord");

	// check zet op een niet grijs vak geldig is volgens verticale en horizontale
	// som van rij stenen.
	else if (!checkGeldigZetVolgensSom(steen, x, y))
	    throw new SpelbordOngeldigeZetSomHogerDanTwaalfException("FoutmeldingSpelvakSomZetHogerDanTwaalf");

	// Check dat zet op grijze steen geldig is
	else if (isGrijsVak(x, y) && !checkGeldigeZetOpGrijsVak(steen, x, y))
	    throw new SpelbordOngeldigeZetGrijsVakException("FoutmeldingOngeldigeZetGrijsVakSpelbord");

	// Indien niet de eerste zet van de beurt: check dat de steen niet als enige
	// buur een andere steen uit dezelfde beurt
	// heeft.
	// Indien wel eerste zet van de beurt: sla locatie op voor check in volgende
	// zet.
	if (zet <= 1) {
	    eersteZetVanBeurtX = x;
	    eersteZetVanBeurtY = y;
	} else {
	    // Check wordt alleen gerund indien beide stenen buren zijn.
	    if (isBuur(x, y, eersteZetVanBeurtX, eersteZetVanBeurtY)) {
		int aantalBuren = 0;
		// Check in de 4 richtingen of er een buur is.
		if (!isOutOfBounds(x - 1, y) && !isVakLeeg(x - 1, y)) {
		    aantalBuren++;
		}
		if (!isOutOfBounds(x + 1, y) && !isVakLeeg(x + 1, y)) {
		    aantalBuren++;
		}
		if (!isOutOfBounds(x, y - 1) && !isVakLeeg(x, y - 1)) {
		    aantalBuren++;
		}
		if (!isOutOfBounds(x, y + 1) && !isVakLeeg(x, y + 1)) {
		    aantalBuren++;
		}

		// Indien aantal buren == 1 -> ongeldige zet
		if (aantalBuren == 1) {
		    throw new SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException(
			    "FoutmeldingOngeldigeZetAlleenEigenBuren");
		}
	    }
	}
    }

    /**
     * Hulpmethode die bepaalt of 2 meegegeven locaties aanpalend zijn.
     * 
     * @param x      X-waarde van eerste locatie
     * @param y      Y-waarde van eerste locatie
     * @param checkX X-waarde van tweede locatie
     * @param checkY Y-waarde van tweede locatie
     * @return true indien aanpalend, anders false.
     */
    private boolean isBuur(int x, int y, int checkX, int checkY) {
	if ((Math.abs(x - checkX) == 1 && y - checkY == 0) || (x - checkX == 0 && Math.abs(y - checkY) == 1)) {
	    return true;
	}
	return false;
    }

    /**
     * Registreer een nieuwe listener voor dit Spelbord.
     * 
     * @param listener Een klasse die de PropertyChangeListener interface
     *                 implementeert.
     */
    public void addListener(@SuppressWarnings("exports") PropertyChangeListener listener) {
	pcs.addPropertyChangeListener(listener);
    }

    /**
     * Verwijder een listener van dit Spelbord.
     * 
     * @param listener Een klasse die de PropertyChangeListener interface
     *                 implementeert, en reeds geregistreerd is bij dit Spelbord.
     */
    public void removeListener(@SuppressWarnings("exports") PropertyChangeListener listener) {
	pcs.removePropertyChangeListener(listener);
    }

    /**
     * Methode die checkt of deze positie buren op de x- of y-as heeft.
     * 
     * @param x x-locatie
     * @param y y-locatie
     * @return true indien positie buren heeft, zoniet false.
     */
    private boolean checkBurenSteen(int x, int y) {
	if ((!isOutOfBoundsXAs(x - 1) && spelbord[x - 1][y].getSteen() != null)
		|| (!isOutOfBoundsXAs(x + 1) && spelbord[x + 1][y].getSteen() != null)
		|| (!isOutOfBoundsYAs(y - 1) && spelbord[x][y - 1].getSteen() != null)
		|| (!isOutOfBoundsYAs(y + 1) && spelbord[x][y + 1].getSteen() != null)) {
	    return true;
	}
	return false;
    }

    /**
     * Check of het meegegeven vak een grijs vak is
     * 
     * @param x x-coordinaat
     * @param y y-coordinaat
     * @return true indien grijs, anders false.
     */
    private boolean isGrijsVak(int x, int y) {

	return spelbord[x][y].getKleur().equals(VakKleur.GRIJS);
    }

    /**
     * Checkt of het vak geblokkeerd is
     * 
     * @param x x-coordinaat
     * @param y y-coordinaat
     * @return true indien geblokkeerd, anders false.
     */
    private boolean isGeblokkeerd(int x, int y) {
	return spelbord[x][y].getKleur().equals(VakKleur.GEBLOKKEERD);
    }

    /**
     * Checkt of de meegegeven coordinaten buiten het spelbord vallen
     * 
     * @param x x-coordinaat
     * @return true indien buiten bord, anders false
     */
    private boolean isOutOfBoundsXAs(int x) {

	return x < 0 || x > spelbord.length - 1;
    }

    /**
     * Checkt of de meegegeven coordinaten buiten het spelbord vallen
     * 
     * @param y y-coordinaat
     * @return true indien buiten bord, anders false
     */
    private boolean isOutOfBoundsYAs(int y) {
	return y < 0 || y > spelbord[0].length - 1;
    }

    /**
     * Checkt of de meegegeven coordinaten buiten het spelbord vallen
     * 
     * @param x x-coordinaat
     * @param y y-coordinaat
     * @return true indien buiten bord, anders false
     */
    private boolean isOutOfBounds(int x, int y) {
	return (isOutOfBoundsXAs(x) && isOutOfBoundsYAs(y));
    }

    /**
     * Check of vak leeg is
     * 
     * @param x x-coordinaat
     * @param y y-coordinaat
     * @return true indien leeg, false indien gevuld
     */
    private boolean isVakLeeg(int x, int y) {
	return geefWaarde(x, y) == 0;
    }

    /**
     * Checkt of meegegeven vak midden van het bord is
     * 
     * @param x x-coordinaat
     * @param y y-coordinaat
     * @return true indien middenvak, anders false
     */
    private boolean isMiddelsteVak(int x, int y) {
	return x == MIDDENPOSITIE_X_SPELBORD && y == MIDDENPOSITIE_Y_SPELBORD;
    }

    /**
     * Hulpmethode die checkt of het mogelijk is om steen op grijs vak te leggen.
     * 
     * @param steen De te leggen steen
     * @param posX  x-positie van de steen
     * @param posY  y-positie van de steen
     * @return true indien mogelijk, anders false.
     */
    private boolean checkGeldigeZetOpGrijsVak(int steen, int posX, int posY) {

	int[] toCheck = berekenSomVanZet(steen, posX, posY);

	return IntStream.of(SCORES_GRIJS_VAK).anyMatch(x -> x == toCheck[0])
		|| IntStream.of(SCORES_GRIJS_VAK).anyMatch(y -> y == toCheck[1]);
    }

    /**
     * Hulpmethode die checkt of het mogelijk is om een steen op niet grijze vak te
     * leggen. Som verticaal of horizontaal mag max 12 zijn met de gelegde steen.
     * 
     * @param steen
     * @param posX
     * @param posY
     * @return
     */
    private boolean checkGeldigZetVolgensSom(int steen, int posX, int posY) {
	int[] toCheck = berekenSomVanZet(steen, posX, posY);

	return toCheck[0] <= MAX_SOM_VAN_STENEN_BIJ_EEN_ZET && toCheck[1] <= MAX_SOM_VAN_STENEN_BIJ_EEN_ZET;
    }

    /**
     * Berekent de som van waarden op de x- en y-as
     * 
     * @param steen waarde van de gelegde steen
     * @param x     x-positie van de steen
     * @param y     y-positie van de steen
     * @return array van integers, positie 0 is de score op de x-as, positie 1 op de
     *         y-as, positie 2 is bonus (1 = grijs vak, 0 is geen grijs vak)
     */
    public int[] berekenSomVanZet(int steen, int posX, int posY) {
	int somX = steen;
	int somY = steen;
	int posToCheck;

	// Check X-as
	// check links
	posToCheck = posX - 1;
	while (!isOutOfBoundsXAs(posToCheck) && !isVakLeeg(posToCheck, posY)) {
	    somX += geefWaarde(posToCheck, posY);
	    posToCheck -= 1;
	}
	// check rechts
	posToCheck = posX + 1;
	while (!isOutOfBoundsXAs(posToCheck) && !isVakLeeg(posToCheck, posY)) {
	    somX += geefWaarde(posToCheck, posY);
	    posToCheck += 1;
	}

	// Check Y-as
	// check boven
	posToCheck = posY - 1;
	while (!isOutOfBoundsYAs(posToCheck) && !isVakLeeg(posX, posToCheck)) {
	    somY += geefWaarde(posX, posToCheck);
	    posToCheck -= 1;
	}
	// check onder
	posToCheck = posY + 1;
	while (!isOutOfBoundsYAs(posToCheck) && !isVakLeeg(posX, posToCheck)) {
	    somY += geefWaarde(posX, posToCheck);
	    posToCheck += 1;
	}

	int[] out = { somX, somY, (isGrijsVak(posX, posY) || isMiddelsteVak(posX, posY)) ? 1 : 0 };
	return out;
    }

    /**
     * Methode die ui kan aanroepen om voor alle ingenomen vakken een update van de
     * listener aan te vragen.
     */
    public void herbouwSpelbord() {
	for (int i = 0; i < spelbord.length; i++) {
	    for (int j = 0; j < spelbord[0].length; j++) {
		if (spelbord[i][j].getSteen() != null) {
		    List<Integer> out = new ArrayList<>();
		    out.add(i);
		    out.add(j);
		    out.add(spelbord[i][j].getSteen().getWaarde());
		    PropertyChangeEvent evt = new PropertyChangeEvent(this, this.getClass().getSimpleName(), null, out);
		    pcs.firePropertyChange(evt);
		}

	    }
	}
    }
}
