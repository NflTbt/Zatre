package domein;

import static domein.DomeinRegels.AANTAL_STENEN_PER_WAARDE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//UC 3
/**
 * Domeinklasse Speelpot te instantieren bij het starten van een spel Zatre met
 * correct aantal stenen per waarde
 * 
 * @author Naoufal Thabet
 */
public class Speelpot {

    private List<Steen> stenen;

    /**
     * Constructor van klasse Speelpot.
     * 
     */
    public Speelpot() {
	setStenen(initialiseerStenenSpel());
	schudStenen();
    }

    /**
     * @return geeft stenen in speelpot
     */
    public List<Steen> getStenen() {
	return stenen;
    }

    /**
     * zet een correct set van stenen.
     * 
     * @param stenen lijst steen objecten
     */
    private void setStenen(List<Steen> stenen) {
	if (stenen == null | stenen.isEmpty()) {
	    throw new IllegalArgumentException();
	}
	this.stenen = stenen;
    }

    /**
     * Hulpmethode om lijst met met stenen te initialiseren.
     * 
     * @return lijst stenen
     */
    private List<Steen> initialiseerStenenSpel() {
	List<Steen> stenen = new ArrayList<>();
	for (int waarde = 1; waarde < AANTAL_STENEN_PER_WAARDE.length; waarde++) {
	    for (int aantal = AANTAL_STENEN_PER_WAARDE[waarde]; aantal > 0; aantal--) {
		stenen.add(new Steen(waarde));
	    }

	}
	return stenen;
    }

    /**
     * Shuffle de lijst stenen willekeurig
     */
    public void schudStenen() {
	if (stenen != null) {
	    Collections.shuffle(stenen);
	}
    }

    /**
     * Geef een aantal stenen terug uit de huidige pot.
     * 
     * @param aantal Aantal te geven stenen. Indien niet genoeg stenen over in de
     *               pot wordt het totale aantal weergegeven.
     * @return Lijst met de gevraagde stenen.
     */
    public List<Steen> geefNieuweStenen(int aantal) {
	if (aantal < 0)
	    throw new IllegalArgumentException("FoutmeldingOngeldigAantalGeefNieuweStenenSpeelpot");
	List<Steen> nieuweStenen = new ArrayList<>();
	if (aantal <= stenen.size()) {
	    for (int i = 0; i < aantal; i++) {
		nieuweStenen.add(stenen.remove(stenen.size() - 1));
	    }
	} else if (stenen.size() == 1) {
	    nieuweStenen.add(stenen.remove(0));
	} else {
	    nieuweStenen = stenen;
	    stenen.clear();
	}
	return nieuweStenen;
    }

    /**
     * Bepaalt of de pot leeg is.
     * 
     * @return boolean leeg of niet.
     */
    public boolean isLeeg() {
	return stenen.isEmpty();
    }

    /**
     * Steekt de meegegeven lijst van stenen terug in de pot.
     * 
     * @param stenen Lijst van terug te leggen stenen.
     */
    public void legStenenTerug(List<Steen> stenen) {
	this.stenen.addAll(stenen);
	schudStenen();
    }

    /**
     * Maakt de speelpot leeg om spel sneller te stoppen.
     */
    public void stopSpel() {
	stenen.clear();
    }
}
