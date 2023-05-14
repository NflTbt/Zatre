package testen;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domein.Scoreblad;

/**
 * JUNIT Test Case voor de klasse Scoreblad
 * 
 * @author Naoufal Thabet
 *
 */
public class ScorebladTest {

    private Scoreblad scoreblad;

    @BeforeEach
    public void before() {
	scoreblad = new Scoreblad();

    }

    /**
     * test of de eindscore van de scoreblad van 1 speler correct wordt berekend.
     */
    @Test
    public void testGeefEindScore() {
	scoreblad.voegScoreBladEntryAanScoreblad(true, 1, 1, 1); // 20
	scoreblad.eindeBeurt();
	scoreblad.voegScoreBladEntryAanScoreblad(true, 1, 1, 1); // 20
	scoreblad.eindeBeurt();
	scoreblad.voegScoreBladEntryAanScoreblad(true, 1, 1, 1); // 20
	scoreblad.eindeBeurt();
	scoreblad.voegScoreBladEntryAanScoreblad(true, 1, 0, 1); // 10
	scoreblad.eindeBeurt();
	scoreblad.voegScoreBladEntryAanScoreblad(true, 1, 0, 1); // 10
	scoreblad.eindeBeurt();
	scoreblad.voegScoreBladEntryAanScoreblad(true, 1, 0, 1); // 10
	scoreblad.eindeBeurt();
	scoreblad.voegScoreBladEntryAanScoreblad(true, 2, 0, 1); // 12
	scoreblad.eindeBeurt();
	scoreblad.voegScoreBladEntryAanScoreblad(true, 0, 0, 1); // 8
	scoreblad.eindeBeurt();
	Assertions.assertEquals(110, scoreblad.geefEindScore()); // 110
    }

}
