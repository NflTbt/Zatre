package gui;

import java.io.IOException;
import java.util.List;

import domein.DomeinController;
import domein.ScorebladEntry;
import exceptions.GeenWinnaarException;
import exceptions.NoSuchPlayerException;
import exceptions.OngeldigeKaraktersException;
import exceptions.SpelNietAfgelopenException;
import exceptions.SpelerLijstLeegException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import taal.Taal;
import taal.Talen;

/**
 * 
 * @author Robin Verplancke
 *
 */

public class EindschermController implements SchermUtility {

    private Talen taal;
    private DomeinController dc;

    @FXML
    private HBox HBoxOverzichtSpelers;

    @FXML
    private Label lblWinnaar;

    @FXML
    private Button btnStartscherm;

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
	btnStartscherm.setText(Taal.getString(taal, "btnStartscherm"));
	btnStartscherm.setOnAction(this::veranderSchermStartscherm);
	HBoxOverzichtSpelers.setSpacing(50);
	maakOverzichtSpelers();
	try {
	    String[] winnaars = dc.geefWinnaar().split("\n");
	    String output = winnaars.length == 1 ? Taal.getString(taal, "lblWinnaar") + ":\n"
		    : Taal.getString(taal, "lblWinnaar") + "s:\n";
	    for (String winnaar : winnaars) {
		output += String.format("%s %s%n", winnaar.split("#")[0],
			Taal.getString(taal, "lblOverzichtSpeler", winnaar.split("#")[1]));
	    }
	    lblWinnaar.setFont(new Font("Arial", 20));
	    lblWinnaar.setAlignment(Pos.CENTER);
	    lblWinnaar.setText(output);
	} catch (SpelNietAfgelopenException | GeenWinnaarException | NoSuchPlayerException
		| OngeldigeKaraktersException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Implemtatie van de methode clearTextFields van de interface SchermUtility.
     */
    @Override
    public void clearTextFields() {
	lblWinnaar.setText("");
    }

    /**
     * Maakt een overzicht van de spelers die het spel gespeeld hebben met elk hun
     * bijpassend scoreblad
     */
    public void maakOverzichtSpelers() {
	try {
	    String[] spelers = dc.geefOverzichtSpelers();
	    List<String> scores = dc.geefScorebladenAlleSpelers();
	    int[] eindscores = dc.geefEindScoresAlleSpelers();

	    for (int i = 0; i < spelers.length; i++) {
		VBox vb = new VBox();
		Label spelerNaam = new Label();
		Label eindscore = new Label();
		TableView<TableScoreEntry> tbvSpeler = initTableView(scores.get(i).split("\n"));
		tbvSpeler.setMaxWidth(420);
		tbvSpeler.getColumns().forEach(c -> c.setMaxWidth(70));
		spelerNaam.setText(spelers[i].split("#")[0]);
		spelerNaam.setFont(new Font("Arial", 25));
		spelerNaam.setPadding(new Insets(0, 0, 20, 0));
		eindscore.setText(String.format("%s %d", Taal.getString(taal, "lblEindscore"), eindscores[i]));
		eindscore.setFont(new Font("Arial", 25));
		eindscore.setPadding(new Insets(20, 0, 0, 0));
		vb.getChildren().addAll(spelerNaam, tbvSpeler, eindscore);
		HBoxOverzichtSpelers.getChildren().add(vb);
	    }

	} catch (SpelerLijstLeegException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Start scherm
     * 
     * @param event
     */
    public void veranderSchermStartscherm(ActionEvent event) {
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
     * Methode maakt een TableView aan en vult deze aan met de verkregen scores voor
     * een speler
     * 
     * @param scores De in te vullen scores in de TableView
     * @return een ingevulde TableView met scores
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private TableView<TableScoreEntry> initTableView(String[] scores) {
	TableView<TableScoreEntry> tbvScoreblad = new TableView<TableScoreEntry>();
	ObservableList<TableScoreEntry> tableData = FXCollections.observableArrayList();

	TableColumn dubbelCol = new TableColumn("x2");
	TableColumn tienCol = new TableColumn("10 (1pt)");
	TableColumn elfCol = new TableColumn("11 (2pt)");
	TableColumn twaalfCol = new TableColumn("12 (4pt)");
	TableColumn bonusCol = new TableColumn("Bonus");
	TableColumn totaalCol = new TableColumn("Totaal");

	dubbelCol.setCellValueFactory(new PropertyValueFactory<ScorebladEntry, String>("dubbel"));
	dubbelCol.setStyle("-fx-alignment: CENTER;");
	tienCol.setCellValueFactory(new PropertyValueFactory<ScorebladEntry, String>("tien"));
	tienCol.setStyle("-fx-alignment: CENTER;");
	elfCol.setCellValueFactory(new PropertyValueFactory<ScorebladEntry, String>("elf"));
	elfCol.setStyle("-fx-alignment: CENTER;");
	twaalfCol.setCellValueFactory(new PropertyValueFactory<ScorebladEntry, String>("twaalf"));
	twaalfCol.setStyle("-fx-alignment: CENTER;");
	bonusCol.setCellValueFactory(new PropertyValueFactory<ScorebladEntry, String>("bonus"));
	bonusCol.setStyle("-fx-alignment: CENTER;");
	totaalCol.setCellValueFactory(new PropertyValueFactory<ScorebladEntry, String>("totaal"));
	totaalCol.setStyle("-fx-alignment: CENTER;");

	tbvScoreblad.getColumns().addAll(dubbelCol, tienCol, elfCol, twaalfCol, bonusCol, totaalCol);
	tbvScoreblad.setMaxWidth(480);
	tableData.clear();
	tbvScoreblad.setItems(tableData);

	for (String lijn : scores) {
	    String[] token = lijn.split("#");
	    if (token.length != 1) {
		tableData.add(new TableScoreEntry(Boolean.parseBoolean(token[0]), Integer.parseInt(token[1]),
			Integer.parseInt(token[2]), Integer.parseInt(token[3]), Integer.parseInt(token[4]),
			Integer.parseInt(token[5])));
		tbvScoreblad.setItems(tableData);
	    }
	}

	return tbvScoreblad;
    }

    /**
     * 
     * @author Robin Verplancke
     *
     */
    public static class TableScoreEntry {

	private final String dubbel;
	private final String tien;
	private final String elf;
	private final String twaalf;
	private final int bonus;
	private final int totaal;

	/**
	 * Deze methode zet een verkregen score entry van het domein om in het visueel
	 * formaat met een of meerdere "X"
	 * 
	 * @param scoreEntry  Boolean voor de score "x2"
	 * @param scoreEntry2 Integer voor de score 10
	 * @param scoreEntry3 Integer voor de score 11
	 * @param scoreEntry4 Integer voor de score 12
	 * @param scoreEntry5 Integer voor de bonusscore
	 * @param scoreEntry6 Integer voor het totaal
	 */
	private TableScoreEntry(boolean scoreEntry, int scoreEntry2, int scoreEntry3, int scoreEntry4, int scoreEntry5,
		int scoreEntry6) {

	    this.dubbel = scoreEntry ? "X" : "";
	    this.tien = (scoreEntry2 == 3) ? "XXX" : (scoreEntry2 == 2) ? "XX" : (scoreEntry2 == 1) ? "X" : "";
	    this.elf = (scoreEntry3 == 3) ? "XXX" : (scoreEntry3 == 2) ? "XX" : (scoreEntry3 == 1) ? "X" : "";
	    this.twaalf = (scoreEntry4 == 3) ? "XXX" : (scoreEntry4 == 2) ? "XX" : (scoreEntry4 == 1) ? "X" : "";
	    this.bonus = scoreEntry5;
	    this.totaal = scoreEntry6;
	}

	/**
	 * 
	 * @return de waarde van dubbel
	 */
	public String isDubbel() {
	    return dubbel;
	}

	/**
	 * 
	 * @return de waarde van tien
	 */
	public String getTien() {
	    return tien;
	}

	/**
	 * 
	 * @return de waarde van elf
	 */
	public String getElf() {
	    return elf;
	}

	/**
	 * 
	 * @return de waarde van twaalf
	 */
	public String getTwaalf() {
	    return twaalf;
	}

	/**
	 * 
	 * @return de waarde van bonus
	 */
	public int getBonus() {
	    return bonus;
	}

	/**
	 * 
	 * @return de waarde van totaal
	 */
	public int getTotaal() {
	    return totaal;
	}
    }
}