package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.ScorebladEntry;

/**
 * JUNIT Test Case voor de klase ScorebladEntry
 * 
 * @author Naoufal Thabet
 *
 */
public class ScorebladEntryTest {

    private ScorebladEntry entryGeldig;

    @BeforeEach
    public void before() {
	entryGeldig = new ScorebladEntry(true, 1, 1, 1, 2);

    }

    /**
     * constructor testen met de aantal aan de hand van geldige waarden
     */
    @Test
    public void testConstructorScorebladEntry_MetGeldigeWaarden() {

	Assertions.assertEquals(true, entryGeldig.isDubbel());
	Assertions.assertEquals(1, entryGeldig.getTien());
	Assertions.assertEquals(1, entryGeldig.getElf());
	Assertions.assertEquals(1, entryGeldig.getTwaalf());
	Assertions.assertEquals(2, entryGeldig.getBonus());

    }

    /**
     * test Constructor met aantal op de geldige initeel waarde 0
     */
    @Test
    public void testConstructorScorebladEntry_MetGeldigeGrensWaardeNul() {
	ScorebladEntry entry = new ScorebladEntry(false, 0, 0, 0, 0);

	Assertions.assertEquals(false, entry.isDubbel());
	Assertions.assertEquals(0, entry.getTien());
	Assertions.assertEquals(0, entry.getElf());
	Assertions.assertEquals(0, entry.getTwaalf());
	Assertions.assertEquals(0, entry.getBonus());

    }

    /**
     * test constructor aantal met maximum waarde voor een integer
     */
    @Test
    public void testConstructorScorebladEntry_MetGeldigeGrensWaardeMaxInteger() {
	ScorebladEntry entry = new ScorebladEntry(false, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
		Integer.MAX_VALUE);

	Assertions.assertEquals(false, entry.isDubbel());
	Assertions.assertEquals(Integer.MAX_VALUE, entry.getTien());
	Assertions.assertEquals(Integer.MAX_VALUE, entry.getElf());
	Assertions.assertEquals(Integer.MAX_VALUE, entry.getTwaalf());
	Assertions.assertEquals(Integer.MAX_VALUE, entry.getBonus());

    }

    /**
     * test constructor met aantal ingesteld op een negatieve waarde, een exception
     * moet gegooid worden.
     */
    @Test
    public void testConstructorScorebladEntry_NegatieveWaardes_ThrowsException() {
	Assertions.assertThrows(IllegalArgumentException.class, () -> new ScorebladEntry(true, -1, -1, -1, -1));
    }

    /**
     * test constructor met aantal ingesteld op een ongeldige negatieve waarde,
     * maximum integer waarde +1 en minimum waarde dat een integer mag hebben.
     */
    @Test
    public void testConstructorScorebladEntry_OngeldigeGrensWaarden() {
	Assertions.assertThrows(IllegalArgumentException.class, () -> new ScorebladEntry(true, Integer.MAX_VALUE + 1,
		Integer.MIN_VALUE, Integer.MAX_VALUE + 1, Integer.MIN_VALUE));
    }

    /**
     * test bereken eindscore 1 entry zonder dubbel of bonus
     */
    public void testBerekenScore_MetDubbelFalseZonderBonus() {
	Assertions.assertEquals(5, new ScorebladEntry(false, 1, 0, 1, 0).geefEindScore());
    }

    /**
     * test bereken eindscore 1 entry zonder dubbel met bonus
     */
    public void testBerekenScore_MetDubbelFalseMetBonus() {
	Assertions.assertEquals(10, new ScorebladEntry(false, 1, 1, 1, 3).geefEindScore());
    }

    /**
     * test bereken eindscore 1 entry zonder dobbel en de alle aantalen en bonus op
     * 0
     */
    public void testBerekenScore_MetDubbelFalseRestEntriesWaardeNul() {
	Assertions.assertEquals(0, new ScorebladEntry(false, 0, 0, 0, 0).geefEindScore());
    }

    /**
     * test bereken eindscore 1 entry met dubbel zonder bonus
     */
    public void testBerekenScore_MetDubbelTrueZonderBonus() {
	Assertions.assertEquals(14, new ScorebladEntry(true, 1, 1, 1, 0).geefEindScore());
    }

    /**
     * test bereken eindscore 1 entry met dubbel met bonus
     */
    public void testBerekenScore_MetDubbelTrueMetBonus() {
	Assertions.assertEquals(20, new ScorebladEntry(true, 1, 1, 1, 3).geefEindScore());
    }

    /**
     * test bereken eindscore 1 entry met dubbel en de alle aantalen en bonus op 0
     */
    public void testBerekenScore_MetDubbelTrueRestEntriesWaardeNul() {
	Assertions.assertEquals(0, new ScorebladEntry(true, 0, 0, 0, 0).geefEindScore());
    }
}