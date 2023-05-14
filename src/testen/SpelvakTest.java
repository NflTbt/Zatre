package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import domein.DomeinRegels.VakKleur;
import domein.Spelvak;
import domein.Steen;

//UC3

/**
 * JUNIT Test Case voor de klasse Spelvak
 * 
 * @author Naoufal Thabet
 *
 */
class SpelvakTest {
    /**
     * testen of spelvak geinstialiseerd wordt met een geldige kleur.
     * 
     * @param kleur kleur mag enkel WIT,GRIJS,ROOD of GEBLOKKEERD
     */
    @ParameterizedTest
    @EnumSource(VakKleur.class)
    public void testConstructorSpelvak_Correct(VakKleur kleur) {
	Assertions.assertDoesNotThrow(() -> new Spelvak(kleur));
    }

    // testen of spelvak geinitialseerd kan worden met een ongelgdige kleur is
    // onmogelijk. Constructor verwacht altijd een waarde in VakKleur.

    /**
     * testen of steen met een geldige waarde op een spelvak kan gezet worden
     * 
     */
    @Test
    public void TestsetSteen_GeldigSteen() {
	Assertions.assertDoesNotThrow(() -> new Spelvak(VakKleur.WIT).setSteen(new Steen(1)));

    }

    /**
     * testen of een steen met een ongeldige waarde op een spelvak kan gezet worden,
     * bij ongeldig waarde moet een IllegalArgumentException gegooid worden.
     * 
     */
    @Test
    public void TestsetSteen_OngeldigSteen_throwsIllegalArgumentException() {
	Assertions.assertThrows(IllegalArgumentException.class, () -> new Spelvak(VakKleur.WIT).setSteen(new Steen(0)));

    }

}
