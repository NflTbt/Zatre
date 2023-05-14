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
import javafx.scene.control.Label;
import javafx.stage.Stage;
import taal.Taal;
import taal.Talen;

/**
 * 
 * @author Robin Verplancke
 * @author Valentijn De Borggrave
 *
 */

public class InstellingenController implements SchermUtility {

    private Talen taal;
    private DomeinController dc;

    @FXML
    private Button btnNederlands;

    @FXML
    private Button btnFrans;

    @FXML
    private Button btnEnglish;

    @FXML
    private Button btnGaTerug;

    @FXML
    private Label lblTaalKeuze;

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar de
     * Engelse taal
     * 
     * @param event
     */
    @FXML
    void setTaalEngels(ActionEvent event) {
	this.taal = Talen.EN;
	initScherm();
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar de
     * Franse taal
     * 
     * @param event
     */
    @FXML
    void setTaalFrans(ActionEvent event) {
	this.taal = Talen.FR;
	initScherm();

    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar de
     * Nederlandse taal
     * 
     * @param event
     */
    @FXML
    void setTaalNederlands(ActionEvent event) {
	this.taal = Talen.NL;
	initScherm();

    }

    /**
     * Implemtatie van de methode setTaal van de interface SchermUtility
     */
    @Override
    public void setTaal(Talen taal) {
	this.taal = taal;
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Startscherm
     * 
     * @param event
     */
    public void veranderSchermStartscherm(ActionEvent event) {
	try {
	    if (SceneNaam.getSceneNaam().equals("speelscherm")) {
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
	    } else {
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
	    }

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
     * Implemtatie van de methode initScherm van de interface SchermUtility
     */
    @Override
    public void initScherm() {
	btnGaTerug.setText(Taal.getString(taal, "btnGaTerug"));
	lblTaalKeuze.setText(Taal.getString(taal, "lblTaalKeuze"));
    }

    /**
     * Implemtatie van de methode clearTextFields van de interface SchermUtility.
     * Dient hier niet geimplementeerd te worden
     */
    @Override
    public void clearTextFields() {
    }

}
