package domein;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DomeinKlasse te instantieren bij het spelen van Zatre.
 * 
 * @author Naoufal Thabet
 * @author Tom Coenen
 */
public class Scoreblad {

    private List<ScorebladEntry> entries;
    private PropertyChangeSupport pcs;
    private int bonusCounter = 0;
    private ScorebladEntry huidigeEntry;

    /**
     * Constructor klasse Scoreblad
     */
    public Scoreblad() {
	entries = new ArrayList<>();
	pcs = new PropertyChangeSupport(this);
    }

    /**
     * Geeft een overzicht van het scoreblad terug.
     * 
     * @return alle scoreblad entries vormen samen een scoreblad.
     */
    public String geefOverzicht() {
	return entries.stream().map(s -> s.geefOverzicht()).collect(Collectors.joining("\n"));
    }

    /**
     * Geeft het scoreblad met eindscore terug.
     * 
     * @return alle scoreblad entries met totale score.
     */
    public String geefOverzichtEindScore() {
	return entries.stream().map(s -> s.geefOverzichtEindScore()).collect(Collectors.joining("\n"));
    }

    /**
     * Toont eindscore van een scoreblad
     * 
     * @return totaal van alle entries van 1 speler
     */
    public int geefEindScore() {
	entries.stream().forEach(ScorebladEntry::berekenEindScore);
	return entries.stream().map((s -> s.geefEindScore())).collect(Collectors.summingInt(Integer::intValue));

    }

    // UC4

    /**
     * Methode voegt 1 scorebladEntry lijn aan scoreblad van 1 speler indien leeg.
     * Zolang de beurt niet voorbij is wordt deze entry verder aangepast. Roep
     * methode eindeBeurt aan om de scorebladentry "final" te maken.
     */
    public void voegScoreBladEntryAanScoreblad(boolean dubbel, int tien, int elf, int twaalf) {
	int bonus;
	switch (bonusCounter) {
	case 0, 1, 2, 3 -> bonus = 3;
	case 4, 5, 6, 7 -> bonus = 4;
	case 8, 9, 10, 11 -> bonus = 5;
	case 12, 13, 14, 15 -> bonus = 6;
	default -> bonus = 0;
	}

	// Indien deze beurt nog geen scores had, maak een nieuwe entry.
	if (huidigeEntry == null) {
	    huidigeEntry = new ScorebladEntry(dubbel, tien, elf, twaalf, bonus);
	}

	// Indien deze beurt reeds een score had, update die lijn.
	else {
	    // Update dubbel
	    if (dubbel) {
		huidigeEntry.setDubbel(dubbel);
	    }

	    // Update 10
	    if (tien > 0) {
		huidigeEntry.setTien(huidigeEntry.getTien() + tien);
	    }

	    // Update 11
	    if (elf > 0) {
		huidigeEntry.setElf(huidigeEntry.getElf() + elf);
	    }

	    // Update 12
	    if (twaalf > 0) {
		huidigeEntry.setTwaalf(huidigeEntry.getTwaalf() + twaalf);
	    }

	    // Verwijder de niet-updated laatste entry uit de lijst.
	    entries.remove(entries.size() - 1);
	}

	entries.add(huidigeEntry);
	firePropertyChange();
    }

    /**
     * Methode aan te roepen wanneer de beurt voorbij is. Laat het scoreblad toe de
     * entry finaal te maken: Kan niet verder updated worden. Bij de volgende
     * scorende beurt wordt dan een nieuwe entry aangemaakt.
     */
    public void eindeBeurt() {
	huidigeEntry = null;
	bonusCounter++;
    }

    /**
     * Registreer een nieuwe listener voor dit Scoreblad.
     * 
     * @param listener Een klasse die de PropertyChangeListener interface
     *                 implementeert.
     */
    public void addListener(@SuppressWarnings("exports") PropertyChangeListener listener) {
	pcs.addPropertyChangeListener(listener);
    }

    /**
     * Verwijder een listener van dit Scoreblad.
     * 
     * @param listener Een klasse die de PropertyChangeListener interface
     *                 implementeert, en reeds geregistreerd is bij dit Scoreblad.
     */
    public void removeListener(@SuppressWarnings("exports") PropertyChangeListener listener) {
	pcs.removePropertyChangeListener(listener);
    }

    /**
     * Stuur update naar listeners, wordt aangeroepen bij wijziging scoreblad.
     */
    public void firePropertyChange() {
	if (!entries.isEmpty()) {
	    pcs.firePropertyChange(this.getClass().getSimpleName(), null, geefOverzicht());
	}
    }

    /**
     * Returns true als het scoreblad leeg is
     * 
     * @return boolean, true indien scoreblad leeg
     */
    public boolean isEmpty() {
	return entries.isEmpty();
    }

}
