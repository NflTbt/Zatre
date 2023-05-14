package domein;

/**
 * Interface met constante domeinregel waarden
 * 
 * @author Tom Coenen
 *
 */
public interface DomeinRegels {

    /**
     * ENUM die de mogeljke kleurwaarden bepaalt voor spelvakken.
     * 
     * @author Tom Coenen
     *
     */
    enum VakKleur {
	WIT, GRIJS, ROOD, GEBLOKKEERD
    }

    /**
     * Kost om een spel te mogen starten
     */
    int SPEELKANSEN_VERLOREN_PER_START_SPEL = 1;

    /**
     * Aantal speelkansen die een speler krijgt als hij wint
     */
    int SPEELKANSEN_GEWONNEN_ALS_WINNAAR = 2;

    /**
     * Minimum leeftijd van een gebruiker
     */
    int MIN_LEEFTIJD = 6;

    /**
     * Maximum leeftijd van een gebruiker
     */
    int MAX_LEEFTIJD = 100;

    /**
     * De minimum lengte van een gebruikersnaam
     */
    int MIN_GEBRUIKERSNAAM_LENGTE = 5;

    /**
     * Standaard speelkansen die ingesteld worden bij het aanmaken van een nieuwe
     * account
     */
    int DEFAULT_SPEELKANSEN = 5;

    /**
     * Minimum aantal spelers voor een spel kan gestart worden
     */
    int MIN_SPELERS = 2;

    /**
     * Maximum aantal actieve spelers
     */
    int MAX_SPELERS = 4;

    /**
     * Een lijst van VakKleur.GEBLOKKEERDe karakters. Huidige lijst bevat << \, ',
     * ", #, &, % >>. Verlaagt het risico van issues in code en/of MySQL injecties.
     * 
     */
    Character[] BLOCKED_CHARACTERS = { '\\', '\'', '\"', '#', '&', '%' };

    /**
     * Array die per positie het aantal stenen bijhoudt voor die waarde: Positie 1
     * voor stenen met waarde 1, positie 2 voor stenen met waarde 2 enz
     */

    /**
     * Minimum waarde van een steen.
     */
    int MIN_WAARDE_STEEN = 1;
    /**
     * Maximum waarde van een steen
     */
    int MAX_WAARDE_STEEN = 6;
    /**
     * array toont de aantal stenen per waarde een spel mag hebben. Index van de
     * array representeert de waarde van een steen binnen Zatre.
     */
    //

    int[] AANTAL_STENEN_PER_WAARDE = { 0, 21, 20, 20, 20, 20, 20 };

    /**
     * spelbord moet exact uit 193 bruikbare vakken. In totaal zijn er 225.
     */
    int AANTAL_SPELVAKKEN = 225;
    int AANTAL_GELDIGE_SPELVAKKEN = 193;

    /**
     * Standaard aantal stenen die speler krijgt per beurt.
     * 
     * De speler die eerste beurt moet spelen krijgt 3, na de eerste beurt van het
     * spel krijgt speler aan beurt iedermaal standaard 2 stenen.
     */
    int AANTAL_NIEUWE_STENEN_EERSTE_BEURT_SPEL = 3;
    int AANTAL_NIEUWE_STENEN_BEURT = 2;

    /**
     * Vereiste score van een zet om een geldige zet te kunnen maken op een grijs
     * vak.
     */
    int[] SCORES_GRIJS_VAK = { 10, 11, 12 };
    int MAX_SOM_VAN_STENEN_BIJ_EEN_ZET = 12;
    /**
     * Dubbele array van ENUM VakKleur met het aan te maken patroon van het
     * speelbord.
     */
    public VakKleur[][] BORDPATROON = {
	    { VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.GRIJS, VakKleur.GEBLOKKEERD, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD },
	    { VakKleur.GEBLOKKEERD, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.GRIJS, VakKleur.GEBLOKKEERD },
	    { VakKleur.GEBLOKKEERD, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS,
		    VakKleur.WIT, VakKleur.GEBLOKKEERD },
	    { VakKleur.GEBLOKKEERD, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.GEBLOKKEERD },
	    { VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT },
	    { VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT },
	    { VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS,
		    VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.GRIJS },
	    { VakKleur.GEBLOKKEERD, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.ROOD, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.GEBLOKKEERD },
	    { VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS,
		    VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.GRIJS },
	    { VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT },
	    { VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT },
	    { VakKleur.GEBLOKKEERD, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.GEBLOKKEERD },
	    { VakKleur.GEBLOKKEERD, VakKleur.WIT, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.GRIJS,
		    VakKleur.WIT, VakKleur.GEBLOKKEERD },
	    { VakKleur.GEBLOKKEERD, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.GRIJS, VakKleur.GEBLOKKEERD },
	    { VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.WIT,
		    VakKleur.WIT, VakKleur.GRIJS, VakKleur.GEBLOKKEERD, VakKleur.GRIJS, VakKleur.WIT, VakKleur.WIT,
		    VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD, VakKleur.GEBLOKKEERD } };

    /**
     * Middelpositie van een spelbord, nodig voor allereerste beurt.
     */
    int MIDDENPOSITIE_X_SPELBORD = DomeinRegels.BORDPATROON[0].length / 2;
    int MIDDENPOSITIE_Y_SPELBORD = DomeinRegels.BORDPATROON.length / 2;

}
