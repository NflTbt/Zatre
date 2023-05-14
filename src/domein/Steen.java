package domein;

//UC3
import static domein.DomeinRegels.MAX_WAARDE_STEEN;
import static domein.DomeinRegels.MIN_WAARDE_STEEN;

/**
 * Domeinklasse te instantieren bij het aanmaken van een steen.
 * 
 * @author Naoufal Thabet
 *
 */
public class Steen {

    private int waarde;

    /**
     * Constructor van klasse Steen
     * 
     * @param waarde elke steen heeft een bepaald waarde tussen 1 en 6
     */
    public Steen(int waarde) {
	setWaarde(waarde);
    }

    /**
     * 
     * @return de waarde van een steen als int.
     */
    public int getWaarde() {
	return this.waarde;
    }

    /**
     * zet een geldige waarde op een steen.
     * 
     * @param waarde minimum 1 maximum 6
     */
    private void setWaarde(int waarde) {
	if (waarde < MIN_WAARDE_STEEN || waarde > MAX_WAARDE_STEEN)
	    throw new IllegalArgumentException("FoutmeldingOngeldigWaardeSteen");
	this.waarde = waarde;
    }

    /**
     * Returnt true indien beide stenen dezelfde zijn.
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Steen other = (Steen) obj;
	return waarde == other.getWaarde();
    }

}
