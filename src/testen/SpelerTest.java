package testen;

import static domein.DomeinRegels.MAX_LEEFTIJD;
import static domein.DomeinRegels.MIN_LEEFTIJD;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import domein.Speler;
import exceptions.OngeldigeKaraktersException;

/**
 * JUNIT Test Case voor de klasse Speler
 * 
 * @author Robin Verplancke
 * @author Tom Coenen
 */

class SpelerTest {

    private Speler speler, speler2;
    private static final String GELDIGE_GEBRUIKERSNAAM = "Robin";
    private static final LocalDate GELDIG_GEBOORTEJAAR = LocalDate.of(1998, 1, 1);
    private static final int GELDIG_AANTAL_SPEELKANSEN = 5;
    private static LocalDate HUIDIGE_DATUM = LocalDate.now();

    /**
     * BeforeEach methode
     * 
     * Deze methode creert voor elke testfunctie 2 speler objecten met een geldige
     * gebruikersnaam en een geldig geboortejaar
     */
    @BeforeEach
    public void before() throws OngeldigeKaraktersException {
	speler = new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIG_GEBOORTEJAAR);
	speler2 = new Speler(GELDIGE_GEBRUIKERSNAAM, GELDIG_GEBOORTEJAAR);
    }

    /**
     * Test methode testConstructorSpeler_Correct
     * 
     * Deze methode test of de constructor van Speler een geldig object aanmaakt
     * Geldig: - een geldige gebruikersnaam - een geldig geboortejaar - een geldig
     * aantal speelkansen
     */

    @Test
    public void testConstructorSpeler_Correct() {
	Assertions.assertEquals(GELDIGE_GEBRUIKERSNAAM, speler.getGebruikersnaam());
	Assertions.assertEquals(GELDIG_GEBOORTEJAAR, speler.getGeboortejaar());
	Assertions.assertEquals(GELDIG_AANTAL_SPEELKANSEN, speler.getSpeelkansen());
    }

    /**
     * ValueSource Test methode voor ongeldigeGebruikersnaam
     * 
     * @param gebruikersnaam opsomming van ongeldige gebruikersnamen, meegegeven in
     *                       een ValueSource Deze methode test of er een exceptie
     *                       gegooid wordt wanneer er een ongeldige gebruikersnaam
     *                       wordt meegegeven aan de constructor van Speler
     */

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "   ", "a", "A", "aaaa", "AAAA", "aaAA", "1234" })
    public void ongeldigeGebruikersnaam(String gebruikersnaam) {
	Assertions.assertThrows(IllegalArgumentException.class, () -> {
	    new Speler(gebruikersnaam, GELDIG_GEBOORTEJAAR);
	});
    }

    /**
     * ValueSource Test methode voor geblokkeerde gebruikersnaam (ongeldige
     * karakters)
     * 
     * @param gebruikersnaam opsomming van ongeldige gebruikersnamen, meegegeven in
     *                       een ValueSource Deze methode test of er een exceptie
     *                       gegooid wordt wanneer er ongeldige karakters worden
     *                       meegegeven aan de constructor van Speler
     */
    @ParameterizedTest
    @ValueSource(strings = { "testen\\", "testen\'", "testen\"", "testen#", "testen&", "testen%" })
    public void geblokkeerdeGebruikersnaam(String gebruikersnaam) {
	Assertions.assertThrows(OngeldigeKaraktersException.class, () -> {
	    new Speler(gebruikersnaam, GELDIG_GEBOORTEJAAR);
	});
    }

    private static Stream<LocalDate> opsommingFoutieveGeboortejaren() {
	return Stream.of(HUIDIGE_DATUM, // leeftijd is nul
		HUIDIGE_DATUM.minusYears(MIN_LEEFTIJD - 1), // net te jong
		HUIDIGE_DATUM.minusYears(MAX_LEEFTIJD + 1)); // net te oud
    }

    /**
     * @param ld opsomming van ongeldige geboortejaren, meegegeven in een static
     *           Stream
     */

    @ParameterizedTest
    @MethodSource("opsommingFoutieveGeboortejaren")
    public void ongeldigeGeboortejaren(LocalDate ld) {
	Assertions.assertThrows(IllegalArgumentException.class, () -> {
	    new Speler(GELDIGE_GEBRUIKERSNAAM, ld);
	});
    }

    /**
     * ValueSource Test methode voor geldigeGebruikersnaam. Deze methode test dat er
     * geen exceptie gegooid wordt wanneer er een geldige gebruikersnaam wordt
     * meegegeven aan de constructor van Speler.
     * 
     * @param gebruikersnaam opsomming van geldige gebruikersnamen, meegegeven in
     *                       een ValueSource
     */

    @ParameterizedTest
    @ValueSource(strings = { "aaaaa", "AAAAA", "12345", "aaAA1" })
    public void geldigeGebruikersnaam(String gebruikersnaam) {
	Assertions.assertDoesNotThrow(() -> {
	    new Speler(gebruikersnaam, GELDIG_GEBOORTEJAAR);
	});
    }

    private static Stream<LocalDate> opsommingGeldigeGeboortejaren() {
	return Stream.of(HUIDIGE_DATUM.minusYears(MIN_LEEFTIJD), // Net oud genoeg
		HUIDIGE_DATUM.minusYears(MAX_LEEFTIJD)); // Net jong genoeg
    }

    /**
     * @param ld opsomming van geldige geboortejaren, meegegeven in een static
     *           Stream
     */

    @ParameterizedTest
    @MethodSource("opsommingGeldigeGeboortejaren")
    public void geldigeGeboortejaren(LocalDate ld) {
	Assertions.assertDoesNotThrow(() -> {
	    new Speler(GELDIGE_GEBRUIKERSNAAM, ld);
	});
    }

    /**
     * Test de equals methode van klasse Speler
     */
    @Test
    public void testObjectsEquals_expectedTrue() {
	Assertions.assertTrue(speler.equals(speler2));
    }

    /**
     * Test de equals methode van klasse Speler
     */
    @Test
    public void testObjectsEquals_expectedFalse() throws OngeldigeKaraktersException {
	Speler speler3 = new Speler(GELDIGE_GEBRUIKERSNAAM + "A", GELDIG_GEBOORTEJAAR);
	Assertions.assertFalse(speler.equals(speler3));

    }
}
