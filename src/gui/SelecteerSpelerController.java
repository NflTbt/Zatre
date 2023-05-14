package gui;

import static domein.DomeinRegels.MAX_LEEFTIJD;
import static domein.DomeinRegels.MIN_GEBRUIKERSNAAM_LENGTE;
import static domein.DomeinRegels.MIN_LEEFTIJD;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import domein.DomeinController;
import exceptions.DubbeleSpelerException;
import exceptions.GeenSpeelkansenException;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
import exceptions.SpelerLijstLeegException;
import exceptions.SpelerLijstTeGrootException;
import exceptions.SpelerLijstTeKleinException;
import exceptions.SpelerNietInLijstException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

public class SelecteerSpelerController implements SchermUtility {

    private Talen taal;
    private DomeinController dc;

    @FXML
    private Button btnGaTerug;

    @FXML
    private Button btnStartSpel;

    @FXML
    private Button btnVerwijder;

    @FXML
    private Button btnVoegToe;

    @FXML
    private ListView<String> lvwGeselecteerdeSpelers;

    @FXML
    private TextField txfGeboortejaar;

    @FXML
    private TextField txfGebruikersnaam;

    @FXML
    private Text txtFoutmeldingGeboortejaar;

    @FXML
    private Text txtFoutmeldingGebruikersnaam;

    @FXML
    private Text txtFoutmeldingSelecteerSpeler;

    @FXML
    private Label lblGeboortejaar;

    @FXML
    private Label lblGebruikersnaam;

    @FXML
    private Label lblGeselecteerdeSpelers;

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Speel Spel scherm
     * 
     * @param event
     */
    @FXML
    void startSpel(ActionEvent event) {

	try {
	    dc.startSpel();

	    try {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("speelscherm.fxml"));
		Parent root = loader.load();
		SpeelschermController speelschermController = loader.getController();
		speelschermController.setDomeinController(dc);
		speelschermController.setTaal(taal);
		speelschermController.initScherm();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	} catch (SpelerLijstLeegException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingSpelerlijstLeeg"));
	} catch (SpelerLijstTeKleinException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingSpelerlijstTeKlein"));
	} catch (DubbeleSpelerException | SpelerLijstTeGrootException | GeenSpeelkansenException
		| OngeldigeKaraktersException | NoSuchPlayerException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Startscherm
     * 
     * @param event
     */
    @FXML
    void veranderSchermStartscherm(ActionEvent event) {

	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("startscherm.fxml"));
	    Parent root = loader.load();
	    StartschermController startschermController = loader.getController();
	    startschermController.setDomeinController(dc);
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
     * Laat toe een actionevent te binden aan een gui element om een speler te
     * verwijderen uit de lijst van de reeds geselecteerde spelers
     * 
     * @param event
     */
    @FXML
    void verwijderSpeler(ActionEvent event) {

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
	} catch (IllegalArgumentException ex) {
	    txtFoutmeldingGeboortejaar.setText(ex.getMessage());
	    return;
	}

	try {
	    dc.verwijderSpeler(gebruikersnaam, geboortejaar);
	    txfGeboortejaar.setText("");
	    txfGebruikersnaam.setText("");

	} catch (OngeldigeKaraktersException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingGeblokkeerdeKarakters"));
	} catch (InputMismatchException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "cuiFoutmeldingOngeldigeKeuze"));
	} catch (SpelerNietInLijstException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingSpelerNietActief"));
	} catch (IllegalArgumentException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, e.getMessage()));
	} catch (SpelerLijstLeegException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingSpelerlijstLeeg"));
	}
	setListView();
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om een speler toe te
     * voegen aan de lijst van geselecteerde spelers om het spel te spelen
     * 
     * @param event
     */
    @FXML
    void voegSpelerToe(ActionEvent event) {

	clearTextFields();

	String gebruikersnaam = null;
	LocalDate geboortejaar = LocalDate.now();

	try {
	    gebruikersnaam = txfGebruikersnaam.getText();

	    // Check ingevoerde username: Niet te kort
	    if (gebruikersnaam.trim().length() < MIN_GEBRUIKERSNAAM_LENGTE) {
		throw new IllegalArgumentException(
			Taal.getString(taal, "cuiFoutmeldingGebruikersnaamTeKort", MIN_GEBRUIKERSNAAM_LENGTE));
	    }

	} catch (IllegalArgumentException ex) {
	    txtFoutmeldingGebruikersnaam.setText(ex.getMessage());
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
	    dc.voegSpelerToe(gebruikersnaam, geboortejaar);
	    txfGebruikersnaam.setText("");
	    txfGebruikersnaam.requestFocus();
	    txfGeboortejaar.setText("");

	} catch (OngeldigeKaraktersException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingGeblokkeerdeKarakters"));
	} catch (DubbeleSpelerException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingDubbeleSpeler"));
	} catch (SpelerLijstTeGrootException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingSpelerlijstVol"));
	} catch (InputMismatchException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "cuiFoutmeldingOngeldigeKeuze"));
	} catch (IllegalArgumentException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, e.getMessage()));
	} catch (NoSuchPlayerException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingSQLNoSuchPlayer"));
	} catch (GeenSpeelkansenException e) {
	    txtFoutmeldingSelecteerSpeler.setText(Taal.getString(taal, "FoutmeldingSpeelkansenOp"));
	}
	setListView();
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
	clearTextFields();

	lblGebruikersnaam.setText(Taal.getString(taal, "lblGebruikersnaam"));
	lblGeboortejaar.setText(Taal.getString(taal, "lblGeboortejaar"));
	btnVoegToe.setText(Taal.getString(taal, "btnVoegToe"));
	btnVerwijder.setText(Taal.getString(taal, "btnVerwijder"));
	btnGaTerug.setText(Taal.getString(taal, "btnGaTerug"));
	lblGeselecteerdeSpelers.setText(Taal.getString(taal, "lblGeselecteerdeSpelers"));
	btnStartSpel.setText(Taal.getString(taal, "btnStartSpel"));
	setListView();

	lvwGeselecteerdeSpelers.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

	    @Override
	    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		if (!lvwGeselecteerdeSpelers.getItems().isEmpty()
			&& lvwGeselecteerdeSpelers.getSelectionModel().getSelectedItem() != null) {
		    String geselecteerdeSpeler = lvwGeselecteerdeSpelers.getSelectionModel().getSelectedItem();
		    String[] tokens = geselecteerdeSpeler.split(",");
		    txfGebruikersnaam.setText(tokens[0].substring(tokens[0].lastIndexOf(" ") + 1));
		    txfGeboortejaar.setText(tokens[1].substring(tokens[1].lastIndexOf(" ") + 1));
		}

	    }
	});

    }

    /**
     * Implemtatie van de methode clearTextFields van de interface SchermUtility.
     */
    @Override
    public void clearTextFields() {
	txtFoutmeldingGebruikersnaam.setText("");
	txtFoutmeldingGeboortejaar.setText("");
	txtFoutmeldingSelecteerSpeler.setText("");

    }

    /**
     * Deze methode zet de correcte format om de geselecteerde spelers voor het spel
     * uit te lezen in de ListView
     */
    private void setListView() {
	try {

	    String[] spelerLijst = dc.geefOverzichtSpelers();
	    List<String> geformateerdeLijst = new ArrayList<>();

	    for (String speler : spelerLijst) {
		String[] tokens = speler.split("#");
		geformateerdeLijst.add(String.format("Speler: %s, Geboortejaar: %s, Speelkansen: %s", tokens[0],
			tokens[1].substring(0, 4), tokens[2]));
	    }

	    ObservableList<String> geselecteerdeSpelers = FXCollections.observableArrayList(geformateerdeLijst);
	    lvwGeselecteerdeSpelers.setItems(geselecteerdeSpelers);

	} catch (SpelerLijstLeegException e) {
	    lvwGeselecteerdeSpelers.getItems().clear();
	}

    }

}
