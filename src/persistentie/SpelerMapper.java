package persistentie;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import domein.Speler;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
import exceptions.PlayerExistsException;

/**
 * Verantwoordelijk voor het aanmaken van Speler objecten uit de databank of
 * deze naar de databank te schrijven
 * 
 * @author Valentijn De Borggrave
 * @author Tom Coenen
 */
class SpelerMapper {
    private static final String INSERT_SPELER = "INSERT INTO ID372477_G107Zatre.Speler (gebruikersnaam, geboortejaar, speelkansen) VALUES (?,?,?)";
    private static final String SELECT_SPELER = "SELECT * FROM ID372477_G107Zatre.Speler WHERE gebruikersnaam = ? AND geboortejaar = ?";
    private static final String UPDATE_SPELER = "UPDATE ID372477_G107Zatre.Speler SET speelkansen = ? WHERE gebruikersnaam = ? AND geboortejaar = ?";

    /**
     * @param speler de toe te voegen speler
     * @throws PlayerExistsException       Wordt gegooid indien de speler reeds in
     *                                     de database aanwezig is.
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     */
    void voegSpelerToe(Speler speler) throws PlayerExistsException, OngeldigeKaraktersException {
	if (checkSpelerBestaat(speler)) {
	    throw new PlayerExistsException();

	} else {
	    try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
		    PreparedStatement query = conn.prepareStatement(INSERT_SPELER)) {

		query.setString(1, speler.getGebruikersnaam());
		query.setDate(2, Date.valueOf(speler.getGeboortejaar()));
		query.setInt(3, speler.getSpeelkansen());
		query.executeUpdate();

	    } catch (SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("SQLState: " + e.getSQLState());
		System.out.println("VendorError: " + e.getErrorCode());
	    }
	}
    }

    /**
     * @param gebruikersnaam de gebruikersnaam van de op te vragen speler
     * @param geboortejaar   het geboortejaar van de op te vragen speler
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     */
    Speler geefSpeler(String gebruikersnaam, LocalDate geboortejaar)
	    throws OngeldigeKaraktersException, NoSuchPlayerException {

	Speler speler = null;

	try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
		PreparedStatement query = conn.prepareStatement(SELECT_SPELER)) {
	    query.setString(1, gebruikersnaam);
	    query.setObject(2, geboortejaar);
	    try (ResultSet rs = query.executeQuery()) {
		if (rs.next()) {
		    String gebruiker = rs.getString("gebruikersnaam");
		    LocalDate geboortedatum = rs.getDate("geboortejaar").toLocalDate();
		    Integer speelkansen = rs.getInt(3);
		    speler = new Speler(gebruiker, geboortedatum, speelkansen);
		} else {
		    throw new NoSuchPlayerException();
		}

	    }
	} catch (SQLException e) {
	    System.out.println("SQLException: " + e.getMessage());
	    System.out.println("SQLState: " + e.getSQLState());
	    System.out.println("VendorError: " + e.getErrorCode());
	}
	return speler;
    }

    /**
     * Checkt of een speler reeds bestaat
     * 
     * @param speler de te controleren speler
     * @return geeft true terug indien deze bestaat (niet null) false indien de
     *         speler niet bestaat (null)
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     */
    private boolean checkSpelerBestaat(Speler speler) throws OngeldigeKaraktersException {

	try {
	    geefSpeler(speler.getGebruikersnaam(), speler.getGeboortejaar());
	    return true;
	} catch (NoSuchPlayerException e) {
	    return false;
	}

    }

    /**
     * Update speelkansen van de speler
     * 
     * @param speler De up te daten speler
     * @throws OngeldigeKaraktersException Wordt gegooid indien geblokkeerde
     *                                     karakters gebruikt worden.
     * @throws NoSuchPlayerException       Exception die gegooid wordt wanneer een
     *                                     speler wordt opgevraagd uit de
     *                                     persistentielaag, maar deze niet gevonden
     *                                     kan worden.
     */
    void updateSpeler(Speler speler) throws OngeldigeKaraktersException, NoSuchPlayerException {
	if (!checkSpelerBestaat(speler)) {
	    throw new NoSuchPlayerException();
	}

	try (Connection conn = DriverManager.getConnection(Connectie.JDBC_URL);
		PreparedStatement query = conn.prepareStatement(UPDATE_SPELER)) {
	    query.setInt(1, speler.getSpeelkansen());
	    query.setString(2, speler.getGebruikersnaam());
	    query.setObject(3, speler.getGeboortejaar());
	    query.executeUpdate();

	} catch (SQLException e) {
	    System.out.println("SQLException: " + e.getMessage());
	    System.out.println("SQLState: " + e.getSQLState());
	    System.out.println("VendorError: " + e.getErrorCode());
	}

    }

}
