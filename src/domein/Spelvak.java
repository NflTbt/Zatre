package domein;

import domein.DomeinRegels.VakKleur;

//UC 3

/**
 * Domeinklasse te instantieren bij het aanmaken van een spelvak.
 * 
 * @author Naoufal Thabet
 *
 */
public class Spelvak {

    private VakKleur kleur;
    private Steen steen;

    /**
     * Constructor voor klasse spelvak.
     * 
     * @param kleur kleur van een spelvak.
     */
    public Spelvak(VakKleur kleur) {
	setKleur(kleur);
    }

    /**
     * 
     * @return kleur van spelvak als VakKleur.
     */
    public VakKleur getKleur() {
	return this.kleur;
    }

    /**
     * 
     * @return steen als Steen.
     */
    public Steen getSteen() {
	return this.steen;
    }

    /**
     * zet een gelige kleur op een spelvak.
     * 
     * @param kleur WIT, GRIJS, ROOD of GEBLOKKEERD.
     */
    private void setKleur(VakKleur kleur) {
	// check in principe overbodig waarde moet uit een Enum komen.
	boolean ongeldigKleur = true;
	for (VakKleur item : VakKleur.values()) {
	    if (!(item.name().equals(kleur.name())))
		ongeldigKleur = true;
	    ongeldigKleur = false;
	}

	if (ongeldigKleur)
	    throw new IllegalArgumentException("FoutmeldingOngeldigKleurOpSpelvak");
	this.kleur = kleur;
    }

    /**
     * zet een steen op een vak.
     * 
     * @param steen moet een instantie zijn van klasse Steen.
     */
    public void setSteen(Steen steen) {
	if (!(steen instanceof Steen))
	    throw new IllegalArgumentException("FoutmeldingOngeldigObjOpSpelvak");
	this.steen = steen;
    }
}
