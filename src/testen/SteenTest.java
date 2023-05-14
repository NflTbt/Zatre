package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Steen;

//UC3

/**
 * JUNIT Test Case voor de klasse Steen
 * 
 * @author Naoufal Thabet
 *
 */
class SteenTest {

    /**
     * testen of steen geinitialiseerd wordt met een geldige waarde
     * @param waarde  opsomming van geldige waardes steen mag een waarde hebben tussen 1 en 6
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })

    public void testConstructorSteen_Correct(int waarde) {
	Assertions.assertDoesNotThrow(() -> new Steen(waarde));
    }

    /**
     * testen of steen geinitialiseerd wordt met een ongeldige waarde, bij ongeldig waarde moet er een IllegalArgumentException gesmeten worden
     * @param waarde  opsomming van ongeldige waardes met oog op de border values tussen geldige en ongeldige waardes.
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 0, 7, Integer.MAX_VALUE, Integer.MIN_VALUE })

    public void testConstructorSteen_InCorrect(int waarde) {
	Assertions.assertThrows(IllegalArgumentException.class, () -> new Steen(waarde));
    }

    /**
     * test equals methode van steen
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
    public void testEquals(int waarde) {
	Steen steen = new Steen(waarde);
	Steen vergelijk = new Steen(waarde);
	Assertions.assertEquals(steen, vergelijk);
    }

}
