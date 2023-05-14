package persistentie;

import java.time.LocalDate;

import domein.Speler;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
import exceptions.PlayerExistsException;

/**
 * Controller voor persistentie package
 * 
 * @author Valentijn De Borggrave
 * @author Tom Coenen
 * 
 */

public class PersistentieController {

    private final SpelerMapper spelerMapper;

    /**
     * Initialiseert spelerMapper aan een nieuw object van de klasse SpelerMapper
     */
    public PersistentieController() {
	spelerMapper = new SpelerMapper();
    }

    /**
     * Roept de methode voegSpelerToe van de SpelerMapper aan om een speler toe te
     * voegen
     * 
     * @param speler De op te slane speler, wordt naar SpelerMapper doorgestuurd
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws PlayerExistsException       Wordt gegooid indien de speler reeds in
     *                                     de database aanwezig is.
     */
    public void voegSpelerToe(Speler speler) throws PlayerExistsException, OngeldigeKaraktersException {
	spelerMapper.voegSpelerToe(speler);
    }

    /**
     * Geeft een speler terug uit de database met gevraagde gebruikersnaam en
     * geboortejaar.
     * 
     * @param gebruikersnaam De gevraagde gebruikersnaam
     * @param geboortejaar   Het gevraagde geboortejaar
     * @return Het object speler dat aan de gevraagde gegevens voldoet
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     */
    public Speler zoekSpeler(String gebruikersnaam, LocalDate geboortejaar)
	    throws OngeldigeKaraktersException, NoSuchPlayerException {
	return spelerMapper.geefSpeler(gebruikersnaam, geboortejaar);
    }

    /**
     * Methode die toelaat een bepaalde Speler te updaten in persistentie.
     * 
     * @param speler De up te daten speler
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     */
    public void updateSpeler(Speler speler) throws OngeldigeKaraktersException, NoSuchPlayerException {
	spelerMapper.updateSpeler(speler);
    }
}