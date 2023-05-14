package domein;

//UC 3

/**
 * DomeinKlasse te instantieren bij het aanmaken van de scorebord wanneer een
 * spel Zatre gestart wordt.
 * 
 * @author Naoufal Thabet
 * @author Tom Coenen
 */
public class ScorebladEntry {

    private boolean dubbel;
    private int tien;
    private int elf;
    private int twaalf;
    private int bonus;
    private int eindScore;

    /**
     * Constructor klasse ScorebladEntry. Scoreblad Entries vormen samen een
     * scoreblad van 1 speler voor het spel Zatre
     * 
     * @param dubbel boolean indien true word som van 1 scoreblad entry verdubbeld.
     * @param tien   voor elke 10 die gevormd wordt een beurt krijgt de speler 1
     *               punt
     * @param elf    voor elke 11 die gevormd wordt een beurt krijgt de speler 2
     *               punt
     * @param twaalf voor elke 12 die gevormd wordt een beurt krijgt de speler 4
     *               punt
     * @param bonus  bijkomende punten een spele kan krijgen bij een entry.
     */
    public ScorebladEntry(boolean dubbel, int tien, int elf, int twaalf, int bonus) {
	setDubbel(dubbel);
	setTien(tien);
	setElf(elf);
	setTwaalf(twaalf);
	setBonus(bonus);
    }

    /**
     * String representatie van een scoreblad entry, waarden worden gescheiden met
     * #.
     * 
     * @return lijn met dubbel, tien, elf, twaalf en bonus.
     */
    public String geefOverzicht() {
	return String.format("%b#%d#%d#%d#%d", dubbel, tien, elf, twaalf, bonus);

    }

    /**
     * String representatie van een scoreblad entry, waarden worden gescheiden met
     * #. Inclusief totaalscore van de entry. Gebruiken voor genereren
     * eindscoreblad.
     * 
     * @return lijn met dubbel, tien, elf, twaalf en bonus.
     */
    public String geefOverzichtEindScore() {
	berekenEindScore();
	return String.format("%b#%d#%d#%d#%d#%d", dubbel, tien, elf, twaalf, bonus, geefEindScore());
    }

    /**
     * Hulpmethode: Berekent de som van 1 scoreblad entry.
     * 
     */
    public void berekenEindScore() {

	eindScore = tien + (elf * 2) + (twaalf * 4);

	if (isVolledigeLijn()) {
	    eindScore += bonus;
	}

	if (dubbel) {
	    eindScore *= 2;
	}

    }

    /**
     * Hulpmethode: Geeft true wanneer een volledige score lijn werd gevormd
     * (minstens 1 van 10,11 en 12)
     * 
     * @return
     */
    public boolean isVolledigeLijn() {
	return tien > 0 && elf > 0 && twaalf > 0;
    }

    /**
     * Geeft de som van 1 scoreblad entry terug.
     * 
     * @return int met som van 1 scoreblad entry
     */
    public int geefEindScore() {
	return eindScore;
    }

    public boolean isDubbel() {
	return dubbel;
    }

    public void setDubbel(boolean dubbel) {
	this.dubbel = dubbel;

    }

    public int getTien() {
	return tien;
    }

    public void setTien(int tien) {
	if (controleerGeldigScorebordEntry(tien)) {
	    this.tien = tien;
	}
    }

    public int getElf() {
	return elf;
    }

    public void setElf(int elf) {
	if (controleerGeldigScorebordEntry(elf)) {
	    this.elf = elf;
	}
    }

    public int getTwaalf() {
	return twaalf;
    }

    public void setTwaalf(int twaalf) {
	if (controleerGeldigScorebordEntry(twaalf)) {
	    this.twaalf = twaalf;
	}
    }

    public int getBonus() {
	return bonus;
    }

    public void setBonus(int bonus) {
	if (controleerGeldigScorebordEntry(bonus)) {
	    this.bonus = bonus;
	}
    }

    /**
     * Hulpmethode controleert of een aantal tien,elf,twaalf en bonus een geldig
     * integer waarde is.
     * 
     * @param aantal mogelijk aantal van tien,elf,twaalf of bonus
     * @return indien aantal geldig is anders wordt er een IllegalArgumentException
     *         gegooid.
     * @throws IllegalArgumentException Wordt gegooid bij een ongeldige entry.
     */
    private boolean controleerGeldigScorebordEntry(int aantal) throws IllegalArgumentException {

	if (aantal < 0)
	    throw new IllegalArgumentException("FoutMeldingNegatiefWaardeScorebladEntry");
	if (!(aantal % 1 == 0))
	    throw new IllegalArgumentException("FoutMeldingWaardeNietIntScorebladEntry");
	return true;
    }

}
