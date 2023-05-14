package testen;

import static domein.DomeinRegels.AANTAL_STENEN_PER_WAARDE;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Speelpot;
import domein.Steen;

//UC 3

/**
 * JUNIT Test Case voor de klasse Speelpot
 * 
 * @author Naoufal Thabet
 * @author Tom Coenen
 *
 */
class SpeelpotTest {

    private Speelpot speelpot;

    @BeforeEach
    public void before() {
	speelpot = new Speelpot();

    }

    /**
     * Testen of totaal aantal stenen per spel gelijk is aan 121
     */
    @Test
    public void testConstructorSpeelpot_CorrectTotaalAantalStenen() {

	assertEquals(speelpot.getStenen().size(), IntStream.of(AANTAL_STENEN_PER_WAARDE).sum()); // 121 stenen
    }

    /**
     * test of aantal stenen van de waarde elk exact zoals gevraagd in DR is
     * 
     * @param waarde waarde van de aangemaakte teststeen
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
    public void testConstructorSpeelpot_CorrectAantalStenenWaarde(int waarde) {
	Steen steen = new Steen(waarde);
	assertEquals(Collections.frequency(speelpot.getStenen(), steen), AANTAL_STENEN_PER_WAARDE[waarde]);
    }

    /**
     * Controleer dat speelpot de correcte aantal stenen initialiseert.
     */
    @Test
    public void controleerLijstStenen() {

	int[] arrayAantalStenen = { 0, 0, 0, 0, 0, 0, 0 };

	for (Steen steen : speelpot.getStenen()) {
	    arrayAantalStenen[steen.getWaarde()]++;
	}

	Assertions.assertArrayEquals(arrayAantalStenen, AANTAL_STENEN_PER_WAARDE);
    }

    /**
     * Test of de methode geefnieuweStenen de aantal stenen uit speelpot verminderd
     * met de aantal stenen die gegeven worden aan een speler
     * 
     * @param aantal
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    public void testGeefNieuweStenen_GeldigAantal(int aantal) {
	int aantalStenenSpeelpotInit = speelpot.getStenen().size();
	List<Steen> nieuweStenen = speelpot.geefNieuweStenen(aantal);
	assertEquals(aantalStenenSpeelpotInit, (speelpot.getStenen().size() + nieuweStenen.size()));

    }

    /**
     * Test of de methode geefnieuweStenen de aantal stenen uit speelpot verminderd
     * met de aantal stenen die gegeven worden aan een speler maar met aantal 0
     * 
     * @param aantal
     */
    @Test
    public void testGeefNieuweStenen_toegelatenBorderValueNul() {
	int aantalStenenSpeelpotInit = speelpot.getStenen().size();
	List<Steen> nieuweStenen = speelpot.geefNieuweStenen(0);
	assertEquals(aantalStenenSpeelpotInit, (speelpot.getStenen().size() + nieuweStenen.size()));
    }

    /**
     * Test of methode geefnieuweStenen een exception gooit wanneer een ongeldig
     * aantal wordt meegegeven
     * 
     * @param aantal
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, Integer.MIN_VALUE, Integer.MIN_VALUE + 1 })
    public void testGeefNieuweStenen_ontoegelatenBorderValueNegatieveAantal_throwsException(int aantal) {

	Assertions.assertThrows(IllegalArgumentException.class, () -> speelpot.geefNieuweStenen(aantal));
    }

    /**
     * Test of methode geefnieuweStenen nog correct werkt wanneer aantal groter of
     * groter is dan aantal stenen in de speelpot
     * 
     * @param aantal
     */
    @ParameterizedTest
    @ValueSource(ints = { 35, 3, 2, 1 })
    public void testGeefNieuweStenen_toegelatenBorderValueResterendAantalspeelpotOfMeer(int aantal) {

	speelpot.geefNieuweStenen(120); // 1 steen overlaten
	int resterendAantalStenen = speelpot.getStenen().size();

	List<Steen> nieuweStenen = speelpot.geefNieuweStenen(aantal); // list nieuweStenen moet altijd nu 1 zijn

	assertEquals(nieuweStenen.size(), resterendAantalStenen);

    }

    /**
     * Test of stenene terug in pot worden teruggelegd.
     */
    @Test
    public void testLegStenenTerug() {
	List<Steen> nieuweStenen = speelpot.geefNieuweStenen(2);
	speelpot.legStenenTerug(nieuweStenen);
	assertEquals(speelpot.getStenen().size(), 121);

    }

}
