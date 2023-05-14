package gui;

import java.io.IOException;

import domein.DomeinController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import taal.Taal;
import taal.Talen;

/**
 * 
 * @author Robin Verplancke
 * @author Valentijn De Borggrave
 *
 */

public class StartschermController implements SchermUtility {

    private Talen taal;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private DomeinController dc;
    private static final Talen DEFAULT_TAAL = Talen.NL;

    @FXML
    private Button btnInstellingen;

    @FXML
    private Button btnRegistreerSpeler;

    @FXML
    private Button btnSpeelSpel;

    /**
     * Methode initialize instantieert de nodige objecten van het domein die
     * doorheen alle GUI schermen gebruikt zullen worden
     */

    public void initialize() {
	setDomeinController(new DomeinController());
	setTaal(DEFAULT_TAAL);
	initScherm();
	SceneNaam.setSceneNaam("startscherm");
    }

    // UC1

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Registreer Speler scherm
     * 
     * @param event
     */
    public void veranderSchermRegistreerSpeler(ActionEvent event) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("registreerspeler.fxml"));
	    root = loader.load();
	    RegistreerSpelerController registreerSpelerController = loader.getController();
	    registreerSpelerController.setTaal(taal);
	    registreerSpelerController.setDomeinController(dc);
	    registreerSpelerController.initScherm();

	} catch (IOException e) {
	    e.printStackTrace();
	}

	scene = new Scene(root);
	stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	stage.setScene(scene);
	stage.show();

    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Instellingen scherm
     * 
     * @param event
     */
    public void veranderSchermInstellingen(ActionEvent event) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("instellingen.fxml"));
	    root = loader.load();
	    InstellingenController instellingenController = loader.getController();
	    instellingenController.setDomeinController(dc);
	    instellingenController.setTaal(taal);
	    instellingenController.initScherm();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	scene = new Scene(root);
	stage.setScene(scene);
	stage.show();
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Selecteer Speler scherm
     * 
     * @param event
     */
    public void veranderSchermSpeelSpel(ActionEvent event) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("selecteerspeler.fxml"));
	    root = loader.load();
	    SelecteerSpelerController selecteerSpelerController = loader.getController();
	    selecteerSpelerController.setDomeinController(dc);
	    selecteerSpelerController.setTaal(taal);
	    selecteerSpelerController.initScherm();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	scene = new Scene(root);
	stage.setScene(scene);
	stage.show();
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
	btnRegistreerSpeler.setText(Taal.getString(taal, "btnRegistreerSpeler"));
	btnSpeelSpel.setText(Taal.getString(taal, "btnSpeelSpel"));
	btnInstellingen.setText(Taal.getString(taal, "btnInstellingen"));
	btnRegistreerSpeler.setOnAction(this::veranderSchermRegistreerSpeler);
	btnSpeelSpel.setOnAction(this::veranderSchermSpeelSpel);
	btnInstellingen.setOnAction(this::veranderSchermInstellingen);

    }

    /**
     * Implemtatie van de methode clearTextFields van de interface SchermUtility.
     * Dient hier niet geimplementeerd te worden
     */
    @Override
    public void clearTextFields() {
    }

}
