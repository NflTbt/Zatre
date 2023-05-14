package domein;

import static domein.DomeinRegels.BLOCKED_CHARACTERS;
import static domein.DomeinRegels.DEFAULT_SPEELKANSEN;
import static domein.DomeinRegels.MAX_LEEFTIJD;
import static domein.DomeinRegels.MIN_GEBRUIKERSNAAM_LENGTE;
import static domein.DomeinRegels.MIN_LEEFTIJD;

import java.time.LocalDate;
import java.util.Arrays;

import exceptions.OngeldigeKaraktersException;

/**
 * Domeinklasse te instantieren bij het aanmaken of ophalen van een speler.
 * 
 * @author Valentijn De Borggrave
 * @author Tom Coenen
 */
public class Speler {

    private String gebruikersnaam;
    private LocalDate geboortejaar;
    private int speelkansen;

    /**
     * Constructor van klasse Speler
     * 
     * @param gebruikersnaam De naam van de te registreren speler
     * @param geboortejaar   Geboortejaar van de geregistreerde speler [YYYY]
     * @throws OngeldigeKaraktersException Wordt gegooid indien de gegeven
     *                                     gebruikersnaam 1 van de geblokkeerde
     *                                     karakters uit de domeinregels bevat.
     */
    public Speler(String gebruikersnaam, LocalDate geboortejaar) throws OngeldigeKaraktersException {
	setGebruikersnaam(gebruikersnaam);
	setGeboortejaar(geboortejaar);
	setSpeelkansen(DEFAULT_SPEELKANSEN);
    }

    /**
     * Constructor van klasse Speler
     * 
     * @param gebruikersnaam De naam van de te registreren speler
     * @param geboortejaar   Geboortejaar van de geregistreerde speler [YYYY]
     * @param speelkansen    Aantal speelkansen van de speler
     * @throws OngeldigeKaraktersException Wordt gegooid indien de gegeven
     *                                     gebruikersnaam 1 van de geblokkeerde
     *                                     karakters uit de domeinregels bevat.
     */
    public Speler(String gebruikersnaam, LocalDate geboortejaar, int speelkansen) throws OngeldigeKaraktersException {
	setGebruikersnaam(gebruikersnaam);
	setGeboortejaar(geboortejaar);
	setSpeelkansen(speelkansen);
    }

    /**
     * 
     * @return gebruikersnaam van de speler
     */
    public String getGebruikersnaam() {
	return gebruikersnaam;
    }

    /**
     * 
     * @return geboortejaar van de speler als LocalDate
     */
    public LocalDate getGeboortejaar() {
	return geboortejaar;
    }

    /**
     * 
     * @return Aantal resterende speelkansen
     */
    public int getSpeelkansen() {
	return speelkansen;
    }

    /**
     * Zet gebruikersnaam van Speler
     * 
     * @param gebruikersnaam String, minimum 5 karakters
     * @throws OngeldigeKaraktersException Wordt gegooid indien de gegeven
     *                                     gebruikersnaam 1 van de geblokkeerde
     *                                     karakters uit de domeinregels bevat.
     */
    private void setGebruikersnaam(String gebruikersnaam) throws OngeldigeKaraktersException {
	if (gebruikersnaam == null || gebruikersnaam.length() < MIN_GEBRUIKERSNAAM_LENGTE || gebruikersnaam.isBlank()) {
	    throw new IllegalArgumentException("cuiFoutmeldingGebruikersnaamTeKort");
	}

	for (Character c : gebruikersnaam.toCharArray()) {
	    if (Arrays.asList(BLOCKED_CHARACTERS).contains(c)) {
		throw new OngeldigeKaraktersException();
	    }
	}
	this.gebruikersnaam = gebruikersnaam;
    }

    /**
     * Zet geboortejaar van Speler
     * 
     * @param geboortejaar LocalDate, minimum 6 jaar en maximum 99 jaar
     * @throws IllegalArgumentException Wordt gegooid indien speler te oud of jong
     *                                  is.
     */

    private void setGeboortejaar(LocalDate geboortejaar) throws IllegalArgumentException {
	LocalDate huidigedatum = LocalDate.now();

	if (huidigedatum.getYear() - geboortejaar.getYear() < MIN_LEEFTIJD) {
	    throw new IllegalArgumentException("cuiFoutmeldingTeJong");
	}

	if (huidigedatum.getYear() - geboortejaar.getYear() > MAX_LEEFTIJD) {
	    throw new IllegalArgumentException("cuiFoutmeldingTeOud");
	}
	this.geboortejaar = geboortejaar;
    }

    /**
     * Zet het aantal speelkansen van de speler, controleert dat speelkansen > 0.
     * 
     * @param speelkansen Aantal speelkansen
     */
    public void setSpeelkansen(int speelkansen) {
	if (speelkansen < 0) {
	    throw new IllegalArgumentException("FoutmeldingSpeelkansenNietNegatief");
	}
	this.speelkansen = speelkansen;
    }

    /**
     * Vergelijkt 2 speler objecten. Returnt true indien ze hetzelfde object zijn,
     * anders false. Gelijke objecten wil zeggen dezelfde gebruikersnaam en
     * geboortejaar.
     * 
     * @param speler Het object te vergelijken
     * @return boolean TRUE indien gelijk, FALSE indien niet gelijk
     */
    @Override
    public boolean equals(Object obj) {
	if (obj == null) {
	    return false;
	}
	if (obj.getClass() != this.getClass()) {
	    return false;
	}
	final Speler speler = (Speler) obj;
	if (speler.getGebruikersnaam() == null | speler.getGeboortejaar() == null) {
	    return false;
	}
	if (!speler.getGebruikersnaam().equals(this.gebruikersnaam)
		|| !speler.getGeboortejaar().equals(this.geboortejaar)) {
	    return false;
	}
	return true;
    }

    /**
     * Geeft de naam van de speler terug. Idem aan getGebruikersnaam().
     */
    @Override
    public String toString() {
	return getGebruikersnaam();
    }
}
