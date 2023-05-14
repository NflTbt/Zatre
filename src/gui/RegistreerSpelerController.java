package gui;

import static domein.DomeinRegels.MAX_LEEFTIJD;
import static domein.DomeinRegels.MIN_GEBRUIKERSNAAM_LENGTE;
import static domein.DomeinRegels.MIN_LEEFTIJD;

import java.io.IOException;
import java.time.LocalDate;
import java.util.InputMismatchException;

import domein.DomeinController;
import domein.DomeinRegels;
import exceptions.OngeldigeKaraktersException;
import exceptions.PlayerExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import taal.Taal;
import taal.Talen;

/**
 * 
 * @author Robin Verplancke
 * @author Valentijn De Borggrave
 *
 */

public class RegistreerSpelerController implements SchermUtility {

    private DomeinController dc;
    private Talen taal;

    @FXML
    private Button btnGaTerug;

    @FXML
    private Button btnRegistreer;

    @FXML
    private Label lblGeboortejaar;

    @FXML
    private Label lblGebruikersnaam;

    @FXML
    private TextField txfGeboortejaar;

    @FXML
    private TextField txfGebruikersnaam;

    @FXML
    private Text txtFoutmeldingGeboortejaar;

    @FXML
    private Text txtFoutmeldingGebruikersnaam;

    @FXML
    private Text txtFoutmeldingRegistreer;

    // UC1
    /**
     * Methode probeert een speler te registreren, deligerend naar het domein via de
     * ingevoerde tekstgegevens. Throws van het domein worden correct afgehandeld.
     * 
     * @param event
     */
    public void registreer(ActionEvent event) {
	String gebruikersnaam = null;
	LocalDate geboortejaar = LocalDate.now();

	clearTextFields();

	try {
	    gebruikersnaam = txfGebruikersnaam.getText();

	    // Check ingevoerde username: Niet te kort
	    if (gebruikersnaam.trim().length() < MIN_GEBRUIKERSNAAM_LENGTE) {
		throw new IllegalArgumentException(
			Taal.getString(taal, "cuiFoutmeldingGebruikersnaamTeKort", MIN_GEBRUIKERSNAAM_LENGTE));
	    }

	} catch (IllegalArgumentException ex) {
	    txtFoutmeldingGebruikersnaam.setText(ex.getMessage());
	    return;
	}

	try {
	    // Check invoer niet leeg
	    if (txfGeboortejaar.getText().isEmpty()) {
		throw new IllegalArgumentException(Taal.getString(taal, "cuiFoutmeldingFouteGeboortejaar"));
	    }

	    geboortejaar = LocalDate.of(Integer.parseInt(txfGeboortejaar.getText()), 1, 1);

	    // Check ingevoerde leeftijd: Niet te jong.
	    if (geboortejaar.plusYears(MIN_LEEFTIJD).isAfter(LocalDate.now())) {
		throw new IllegalArgumentException(Taal.getString(taal, "cuiFoutmeldingTeJong"));
	    }
	    // Check ingevoerde leeftijd: Niet te oud.
	    if (geboortejaar.isBefore(LocalDate.now().minusYears(MAX_LEEFTIJD))) {
		throw new IllegalArgumentException(Taal.getString(taal, "cuiFoutmeldingTeOud", MAX_LEEFTIJD));
	    }

	} catch (InputMismatchException e) {
	    txtFoutmeldingGeboortejaar.setText(Taal.getString(taal, "FoutmeldingScannerOngeldigeInvoer"));
	    return;

	} catch (NumberFormatException e) {
	    txtFoutmeldingGeboortejaar.setText(Taal.getString(taal, "FoutmeldingGeenCorrecteDatum"));
	    return;
	} catch (IllegalArgumentException ex) {
	    txtFoutmeldingGeboortejaar.setText(ex.getMessage());
	    return;
	}

	try {
	    dc.registreer(gebruikersnaam, geboortejaar);
	    txtFoutmeldingRegistreer
		    .setText(String.format("%s, %d %s: %s", dc.geefGebruikersnaam(), dc.geefSpeelkansen(),
			    Taal.getString(taal, "cuiSpeelkansen"), Taal.getString(taal, "cuiRegistratieGeslaagd")));
	    txfGebruikersnaam.setText("");
	    txfGeboortejaar.setText("");

	} catch (PlayerExistsException e) {
	    txtFoutmeldingRegistreer.setText(Taal.getString(taal, "FoutmeldingSQLSpelerBestaatAl"));
	} catch (OngeldigeKaraktersException e) {
	    txtFoutmeldingRegistreer.setText(Taal.getString(taal, "FoutmeldingGeblokkeerdeKarakters",
		    (Object[]) DomeinRegels.BLOCKED_CHARACTERS));
	}
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Startscherm
     * 
     * @param event
     */
    public void veranderSchermStartscherm(ActionEvent event) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("startscherm.fxml"));
	    Parent root = loader.load();
	    StartschermController startschermController = loader.getController();
	    startschermController.setTaal(taal);
	    startschermController.initScherm();
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Implemtatie van de methode setDomeinController van de interface SchermUtility
     */
    @Override
    public void setDomeinController(DomeinController dc) {
	this.dc = dc;
    }

    /**
     * Implemtatie van de methode setTaal van de interface SchermUtility
     */
    @Override
    public void setTaal(Talen taal) {
	this.taal = taal;
    }

    /**
     * Implemtatie van de methode initScherm van de interface SchermUtility
     */
    @Override
    public void initScherm() {
	lblGebruikersnaam.setText(Taal.getString(taal, "lblGebruikersnaam"));
	lblGeboortejaar.setText(Taal.getString(taal, "lblGeboortejaar"));
	btnRegistreer.setText(Taal.getString(taal, "btnRegistreerSpeler"));
	btnGaTerug.setText(Taal.getString(taal, "btnGaTerug"));
	txtFoutmeldingGebruikersnaam.setText("");
	txtFoutmeldingGeboortejaar.setText("");
	txtFoutmeldingRegistreer.setText("");
	btnRegistreer.setOnAction(this::registreer);
	btnGaTerug.setOnAction(this::veranderSchermStartscherm);

    }

    /**
     * Implemtatie van de methode clearTextFields van de interface SchermUtility.
     */
    @Override
    public void clearTextFields() {
	txtFoutmeldingGebruikersnaam.setText("");
	txtFoutmeldingGeboortejaar.setText("");
	txtFoutmeldingRegistreer.setText("");
    }
}
