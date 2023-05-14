package testen;

import static domein.DomeinRegels.AANTAL_GELDIGE_SPELVAKKEN;
import static domein.DomeinRegels.AANTAL_SPELVAKKEN;
import static domein.DomeinRegels.BORDPATROON;
import static domein.DomeinRegels.MIDDENPOSITIE_X_SPELBORD;
import static domein.DomeinRegels.MIDDENPOSITIE_Y_SPELBORD;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import domein.DomeinRegels.VakKleur;
import domein.Spelbord;
import domein.Spelvak;
import domein.Steen;
import exceptions.SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException;
import exceptions.SpelbordOngeldigeZetGeblokkeerdVakException;
import exceptions.SpelbordOngeldigeZetGrijsVakException;
import exceptions.SpelbordOngeldigeZetNietAanpalendException;
import exceptions.SpelbordOngeldigeZetNietInMiddenException;
import exceptions.SpelbordOngeldigeZetSomHogerDanTwaalfException;
import exceptions.SpelbordOngeldigeZetVakNietLeegException;

/**
 * JUNIT Test Case voor de klasse Spelbord
 * 
 * @author Naoufal Thabet
 * @author Tom Coenen
 */
class SpelbordTest {

    private Spelbord spelbord;
    private Steen steen;
    private Spelvak[][] bord;

    @BeforeEach
    public void before() {
	spelbord = new Spelbord();
	steen = new Steen(1);
	bord = spelbord.getSpelbord();
    }

    /**
     * Constructor Spelbord testen. Spelbord moet gegenereerd worden volgens
     * DomeinRegels.BORDPATROON
     * 
     */

    @Test
    public void testConstructor_GeeftTrue() {
	Assertions.assertEquals(spelbord.getSpelbord().length, BORDPATROON.length);
    }

    /**
     * Constructor spelbord vergelijken met een ongeldig patroon van een spelbord.
     * 
     */
    @Test
    public void testConstructor_VergelijkenOngeldigPatroon_GeeftFalse() {

	Spelvak[][] ongeldigSpelbord = { { new Spelvak(VakKleur.WIT) }, { new Spelvak(VakKleur.GRIJS) } };
	Assertions.assertFalse(Arrays.equals(spelbord.getSpelbord(), ongeldigSpelbord));

    }

    /**
     * Check dat spelbord correct aantal totale en geldige vakken heeft
     */
    @Test
    public void checkSpelbordGeldig() {
	// Check aantal totale vakken
	Assertions.assertEquals(Arrays.stream(spelbord.getSpelbord()).flatMap(Arrays::stream).count(),
		AANTAL_SPELVAKKEN);

	// Check aantal geldige vakken
	Assertions.assertEquals(Arrays.stream(spelbord.getSpelbord()).flatMap(Arrays::stream)
		.filter(vak -> vak.getKleur() != VakKleur.GEBLOKKEERD).count(), AANTAL_GELDIGE_SPELVAKKEN);
    }

    // UC 4

    /**
     * Simpel test om te controlleren of een steen effectief werd geplaatste op een
     * Spelbord.
     */
    @Test
    public void testLeesWaardeSteenOpVakVanSpelbord() {
	bord[4][0].setSteen(new Steen(1));

	assertEquals(1, spelbord.geefWaarde(4, 0));
    }

    /**
     * Check of de aller eeste steen van een spel exact in het middenste van het
     * bord wordt gelegd
     */
    @Test
    public void testLegSteen_eersteZet_geldig_steenExactInHetMidden() {
	if (spelbord.isBordLeeg())
	    Assertions.assertDoesNotThrow(
		    () -> spelbord.legSteen(steen, MIDDENPOSITIE_X_SPELBORD, MIDDENPOSITIE_Y_SPELBORD, 0));

    }

    /**
     * Check of er een exception wordt gegooid wanneer de allereerste steen niet
     * exact in het middenste vak van het bord wordt gelegd
     */
    @Test
    public void testLegSteen_eersteZet_ongeldig_ThrowsException() {
	if (spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetNietInMiddenException.class,
		    () -> spelbord.legSteen(steen, 4, 0, 0));
    }

    /**
     * Check of er een exception wordt gegooid wanneer met probeert een steen te
     * leggen op vak dat al reeds bezt is met een steen.
     */
    @Test
    public void testLegSteen_ongeldigZet_NietLeegVak_ThrowsException() {

	bord[4][1].setSteen(new Steen(5));

	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetVakNietLeegException.class,
		    () -> spelbord.legSteen(steen, 4, 1, 2));

    }

    /**
     * Check of er een exception wordt gegooid wanneer men steen probeert te leggen
     * op een geblokkeerde vak.
     */
    @Test
    public void testLegSteen_ongeldigZet_geblokkeerdeVak_ThrowsException() {

	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetGeblokkeerdVakException.class,
		    () -> spelbord.legSteen(steen, 0, 0, 2));
    }

    /**
     * Check of er een exception wordt gegooid wanneer er een ongeldige x coordinaat
     * wordt opgegeven. Steen kan niet gelegd worden buiten het spelbord.
     * 
     * @param x x-coördinaat
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 17 })
    public void testLegSteen_ongeldigZet_buitenXAS_ThrowsException(int x) {
	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(IllegalArgumentException.class, () -> spelbord.legSteen(steen, x, 0, 2));
    }

    /**
     * Check of er een exception wordt gegooid wanneer er een ongeldige x coordinaat
     * wordt opgegeven. Steen kan niet gelegd worden buiten het spelbord.
     * 
     * @param y y-coördinaat
     */
    @ParameterizedTest
    @ValueSource(ints = { -1, 17 })
    public void testLegSteen_ongeldigZet_buitenYAS_ThrowsException(int y) {
	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(IllegalArgumentException.class, () -> spelbord.legSteen(steen, 6, y, 2));
    }

    /**
     * Check op een zet op een leeg vak waar de gevormde som horizontaal en
     * verticaal niet hoger dan 12 is.
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
    public void testLegSteen_geldigZet_OpLeegNietGrijsVak(int waarde) {
	Steen teLeggenSteen = new Steen(waarde);

	bord[4][1].setSteen(new Steen(5));
	bord[5][1].setSteen(new Steen(1));

	if (!spelbord.isBordLeeg())
	    Assertions.assertDoesNotThrow(() -> spelbord.legSteen(teLeggenSteen, 6, 1, 2));

    }

    /**
     * Check of er een exception wordt gegooid wanneer men steen legt op een leeg
     * vak maar de gevormde som horizontaal is hoger dan 12
     * 
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
    public void testLegSteen_ongeldigZet_OpLeegVakSomHogerDan12Horizontaal_ThrowsException(int waarde) {
	Steen teLeggenSteen = new Steen(waarde);

	bord[4][1].setSteen(new Steen(6));
	bord[5][1].setSteen(new Steen(6));

	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetSomHogerDanTwaalfException.class,
		    () -> spelbord.legSteen(teLeggenSteen, 6, 1, 2));

    }

    /**
     * Check of er een exception wordt gegooid wanneer men steen legt op een leeg
     * vak maar de gevormde som verticaal is hoger dan 12
     * 
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
    public void testLegSteen_ongeldigZet_OpLeegVakSomHogerDan12Verticaal_ThrowsException(int waarde) {
	Steen teLeggenSteen = new Steen(waarde);

	bord[4][0].setSteen(new Steen(6));
	bord[4][1].setSteen(new Steen(6));

	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetSomHogerDanTwaalfException.class,
		    () -> spelbord.legSteen(teLeggenSteen, 4, 2, 2));

    }

    /**
     * Check of een steen gelegd kan worden op grijze vak wanneer de som van de
     * stenen horizontaal 10, 11 of 12 vormen
     * 
     * @param waarde waarde van de Steen.
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    public void testLegSteen_geldigZet_OpLeegGrijsVak_horizontaal(int waarde) {
	Steen teLeggenSteen = new Steen(waarde);

	bord[2][1].setSteen(new Steen(4));
	bord[3][1].setSteen(new Steen(5));

	if (!spelbord.isBordLeeg())
	    Assertions.assertDoesNotThrow(() -> spelbord.legSteen(teLeggenSteen, 1, 1, 2));

    }

    /**
     * Check of een steen gelegd kan worden op grijze vak wanneer de som van de
     * stenen verticaal 10, 11 of 12 vormen
     * 
     * @param waarde waarde van de Steen.
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    public void testLegSteen_geldigZet_OpLeegGrijsVak_verticaal(int waarde) {
	Steen teLeggenSteen = new Steen(waarde);

	bord[1][2].setSteen(new Steen(4));
	bord[1][3].setSteen(new Steen(5));

	if (!spelbord.isBordLeeg())
	    Assertions.assertDoesNotThrow(() -> spelbord.legSteen(teLeggenSteen, 1, 1, 2));

    }

    /**
     * Check of er een exception wordt gegooid wanneer een steen op een grijze vak
     * gelegd word en som van de gevormde stenen horizontaal minder dan 10 is. Er
     * mag enkel op een grijs vak geplaatst worden wanneer er een 10,11, of 12
     * gevormd wordt.
     * 
     * @param waarde waarde van de Steen.
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
    public void testLegSteen_onggeldigZet_OpLeegGrijsVak_gevormdeSomMinderDan9Horizontaal_ThrowsException(int waarde) {
	Steen teLeggenSteen = new Steen(waarde);

	bord[2][1].setSteen(new Steen(1));

	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetGrijsVakException.class,
		    () -> spelbord.legSteen(teLeggenSteen, 1, 1, 2));
    }

    /**
     * Check of er een exception wordt gegooid wanneer een steen op een grijze vak
     * gelegd word en de som van de gevormde stenen verticaal minder dan 10 is. Er
     * mag enkel op een grijs vak geplaatst worden wanneer er een 10,11, of 12
     * gevormd wordt.
     * 
     * @param waarde
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3, 4, 5, 6 })
    public void testLegSteen_onggeldigZet_OpLeegGrijsVak_gevormdeSomMinderDan9Verticaal_ThrowsException(int waarde) {
	Steen teLeggenSteen = new Steen(waarde);

	bord[1][2].setSteen(new Steen(1));

	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetGrijsVakException.class,
		    () -> spelbord.legSteen(teLeggenSteen, 1, 1, 2));
    }

    /**
     * Check of er een excption wordt gegooid wanneer een steen word geplaatst op
     * vak waar geen aanpallende stenen liggen.
     * 
     * @param x x-coördinaat
     */
    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 })
    public void testLegSteen_ongeldigZet_leegVak_geenBuren_ThrowsException(int x) {
	if (!spelbord.isBordLeeg())
	    Assertions.assertThrows(SpelbordOngeldigeZetNietAanpalendException.class,
		    () -> spelbord.legSteen(steen, x, 1, 2));

    }

    /**
     * Check of er een exception gegooid wordt wanneer een steen gelegd wordt op vak
     * dat enkel aanpalend is met een zojuist gelegde steen uit dezelfde beurt.
     * 
     * @param y
     * @throws SpelbordOngeldigeZetVakNietLeegException
     * @throws SpelbordOngeldigeZetGeblokkeerdVakException
     * @throws SpelbordOngeldigeZetNietAanpalendException
     * @throws SpelbordOngeldigeZetGrijsVakException
     * @throws SpelbordOngeldigeZetNietInMiddenException
     * @throws SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException
     * @throws SpelbordOngeldigeZetSomHogerDanTwaalfException
     */
    @ParameterizedTest
    @ValueSource(ints = { 0, 2 })
    public void testLegSteen_ongeldigZet_aanpalendeBuurKomtUitDezelfdeBeurt_ThrowsException(int y)
	    throws SpelbordOngeldigeZetVakNietLeegException, SpelbordOngeldigeZetGeblokkeerdVakException,
	    SpelbordOngeldigeZetNietAanpalendException, SpelbordOngeldigeZetGrijsVakException,
	    SpelbordOngeldigeZetNietInMiddenException, SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException,
	    SpelbordOngeldigeZetSomHogerDanTwaalfException {
	if (!spelbord.isBordLeeg()) {
	    bord[3][1].setSteen(new Steen(1));

	    spelbord.legSteen(new Steen(2), 4, 1, 2);

	    Assertions.assertThrows(SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException.class,
		    () -> spelbord.legSteen(steen, 4, y, 3));
	}
    }

}
