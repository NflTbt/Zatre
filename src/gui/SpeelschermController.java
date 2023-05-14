package gui;

import static domein.DomeinRegels.BORDPATROON;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domein.DomeinController;
import domein.DomeinRegels.VakKleur;
import domein.ScorebladEntry;
import exceptions.SpelSpelerHeeftDezeSteenNietException;
import exceptions.SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException;
import exceptions.SpelbordOngeldigeZetGeblokkeerdVakException;
import exceptions.SpelbordOngeldigeZetGrijsVakException;
import exceptions.SpelbordOngeldigeZetNietAanpalendException;
import exceptions.SpelbordOngeldigeZetNietInMiddenException;
import exceptions.SpelbordOngeldigeZetSomHogerDanTwaalfException;
import exceptions.SpelbordOngeldigeZetVakNietLeegException;
import exceptions.SpelerLijstLeegException;
import exceptions.SpelerNietInLijstException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import taal.Taal;
import taal.Talen;

/**
 * 
 * @author Robin Verplancke
 * @author Wim Dhaenens
 *
 */

public class SpeelschermController implements SchermUtility, PropertyChangeListener {

    private DomeinController dc;
    private Talen taal;
    private ImageView vakken[][] = new ImageView[15][15];
    private Image vakPicture[][] = new Image[15][15];
    private Media musicFileEindebeurt = new Media(getClass().getResource("/audio/volgendespeler.wav").toString());
    private Media musicFileDrop = new Media(getClass().getResource("/audio/drop.wav").toString());
    private Media musicFilefout = new Media(getClass().getResource("/audio/foutje.wav").toString());
    private MediaPlayer mediaPlayerEindebeurt = new MediaPlayer(musicFileEindebeurt);
    private MediaPlayer mediaPlayerFileDrop = new MediaPlayer(musicFileDrop);
    private MediaPlayer mediaPlayerError = new MediaPlayer(musicFilefout);
    private ObservableList<TableScoreEntry> tableData = FXCollections.observableArrayList();
    private boolean isRegistered = false;

    @FXML
    private TableView<TableScoreEntry> tbvScoreblad = new TableView<TableScoreEntry>();

    @FXML
    private Button btnEindeBeurt;

    @FXML
    private Button btnGaTerug;

    @FXML
    private Button btnInstellingen;

    @FXML
    private Button btnStopSpel;

    @FXML
    private Label lblNaamSpeler;

    @FXML
    private Label lblSpelerAanDeBeurt;

    @FXML
    private Label lblMijnStenen;

    @FXML
    private Label lblFoutMelding;

    @FXML
    private Label lblAantalStenen;

    @FXML
    private HBox HBoxStenen;

    @FXML
    private GridPane gameBord;

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
	initTableView();
	maakBord();
	if (!isRegistered) {
	    dc.registreerListenerSpelbord(this);
	    registreerScorebladenSpelers();
	}

	SceneNaam.setSceneNaam("speelscherm");

	btnGaTerug.setText(Taal.getString(taal, "btnGaTerug"));
	lblSpelerAanDeBeurt.setText(Taal.getString(taal, "lblSpelerAanBeurt"));
	btnEindeBeurt.setText(Taal.getString(taal, "btnEindeBeurt"));
	lblMijnStenen.setText(Taal.getString(taal, "lblMijnStenen").toUpperCase());

	btnGaTerug.setOnAction(this::veranderSchermSpeelSpel);
	btnInstellingen.setOnAction(this::veranderSchermInstellingen);
	btnEindeBeurt.setOnAction(this::eindeBeurt);
	btnStopSpel.setOnAction(this::stopSpel);

	HBoxStenen.setSpacing(50);

	dc.herbouwSpelbord();
	speelBeurt();
    }

    /**
     * Methode die voor elke speler van het spel in listener instelt op zijn/haar
     * bijhorend scoreblad
     */
    public void registreerScorebladenSpelers() {
	try {
	    String[] spelers = dc.geefOverzichtSpelers();
	    for (String speler : spelers) {
		String[] tokens = speler.split("#");
		try {
		    dc.registreerListenerScoreblad(this, tokens[0], Integer.parseInt(tokens[1].split("-")[0]));
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		} catch (SpelerNietInLijstException e) {
		    e.printStackTrace();
		}
	    }

	} catch (SpelerLijstLeegException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Methode voor de sequentie van een spelbeurt
     */
    public void speelBeurt() {
	lblNaamSpeler.setText(dc.geefSpelerAanDeBeurt());
	lblAantalStenen.setText(String.format("%d", dc.geefAantalStenenResterend()));
	vulDobbelstenenOp();
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om eind spel te
     * triggeren. In dit geval zal de methode veranderSchermEindSpel opgeroepen
     * worden
     * 
     * @param event
     */
    public void eindeBeurt(ActionEvent event) {
	if (!dc.isEindeSpel()) {
	    tableData.clear();
	    try {
		dc.legStenenTerugInSpeelpot();
	    } catch (SpelerLijstLeegException e) {
		e.printStackTrace();
	    }
	    mediaPlayerEindebeurt.seek(mediaPlayerEindebeurt.getStartTime());
	    mediaPlayerEindebeurt.play();
	    speelBeurt();
	} else {
	    veranderSchermEindeSpel(event);
	}
    }

    /**
     * Implemtatie van de methode clearTextFields van de interface SchermUtility.
     */
    @Override
    public void clearTextFields() {
	lblFoutMelding.setText("");
    }

    /**
     * Initialiseert een TableView waarin de scorebladen zullen worden weergegeven
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void initTableView() {
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
	tableData.clear();
	tbvScoreblad.setItems(tableData);
	tbvScoreblad.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Maakt het bord aan volgens het bordpatroon van de domeinregels
     */
    private void maakBord() {

	for (int kol = 0; kol < BORDPATROON.length; kol++) {
	    for (int rij = 0; rij < BORDPATROON.length; rij++) {

		vakPicture[kol][rij] = new Image(
			getClass().getResourceAsStream("/Pictures/" + BORDPATROON[rij][kol] + ".png"));

		vakken[kol][rij] = new ImageView(vakPicture[kol][rij]);

		if (BORDPATROON[rij][kol] == VakKleur.GEBLOKKEERD) {
		    vakken[kol][rij].setFitHeight(64);
		    vakken[kol][rij].setFitWidth(64);
		} else {
		    vakken[kol][rij].setFitHeight(64);
		    vakken[kol][rij].setFitWidth(64);
		}

		vakken[kol][rij].setId("kol=" + kol + "&rij=" + rij + "&kleur=" + BORDPATROON[rij][kol]);

		maakDropbaar(kol, rij);
		gameBord.add(vakken[kol][rij], kol, rij);
	    }
	}
    }

    /**
     * Maakt de dobbelstenen aan van de huidige actieve speler en toont deze op het
     * scherm
     */
    private void vulDobbelstenenOp() {
	int[] stenen = dc.geefStenenSpelerAanDeBeurt();
	HBoxStenen.getChildren().clear();
	for (int i = 0; i < stenen.length; i++) {
	    ImageView dobbelsteen = new ImageView(
		    new Image(getClass().getResourceAsStream("/Pictures/" + stenen[i] + ".png")));
	    dobbelsteen.setId(String.format("%d", stenen[i]));
	    HBoxStenen.getChildren().add(dobbelsteen);
	    maakDragbaar(dobbelsteen, HBoxStenen);
	}
    }

    /**
     * Deze methode maakt een GUI element dropbaar. M.a.w. op dit element zullen
     * andere elementen kunnen worden gedropt.
     * 
     * @param kol Kolom op het bord
     * @param rij Rij op het bord
     */
    private void maakDropbaar(int kol, int rij) {
	vakken[kol][rij].setOnDragOver(new EventHandler<DragEvent>() {
	    @Override
	    public void handle(DragEvent event) {
		if (event.getGestureSource() != vakken[kol][rij] && event.getDragboard().hasImage()) {
		    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		}
		event.consume();
	    }

	});
	vakken[kol][rij].setOnDragDropped(new EventHandler<DragEvent>() {
	    @Override
	    public void handle(DragEvent event) {
		Dragboard db = event.getDragboard();
		boolean success = false;
		try {
		    dc.legSteen(Integer.parseInt(db.getString()), rij, kol);
		    vulDobbelstenenOp();
		    stopDropbaar(kol, rij);
		    vakken[kol][rij].setId(kol + "&" + rij + "&" + db.getString());

		} catch (SpelSpelerHeeftDezeSteenNietException e) {
		    lblFoutMelding.setText(Taal.getString(taal, "FoutmeldingSpelSpelerHeeftDezeSteenNiet"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		} catch (SpelbordOngeldigeZetNietInMiddenException e) {
		    lblFoutMelding.setText(Taal.getString(taal, "FoutmeldingSpelbordOngeldigeZetNietInMidden"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		} catch (SpelbordOngeldigeZetVakNietLeegException e) {
		    lblFoutMelding.setText(Taal.getString(taal, "FoutmeldingSpelbordOngeldigeZetVakNietLeeg"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		} catch (SpelbordOngeldigeZetGeblokkeerdVakException e) {
		    lblFoutMelding.setText(Taal.getString(taal, "FoutmeldingSpelbordOngeldigeZetGeblokkeerdVak"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		} catch (SpelbordOngeldigeZetNietAanpalendException e) {
		    lblFoutMelding.setText(Taal.getString(taal, "FoutmeldingSpelbordOngeldigeZetNietAanpalend"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		} catch (SpelbordOngeldigeZetSomHogerDanTwaalfException e) {
		    lblFoutMelding.setText(Taal.getString(taal, "FoutmeldingSpelvakSomZetHogerDanTwaalf"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		} catch (SpelbordOngeldigeZetGrijsVakException e) {
		    lblFoutMelding.setText(Taal.getString(taal, "FoutmeldingSpelbordOngeldigeZetGrijsVak"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		} catch (SpelbordOngeldigeZetAlleenEigenSteenAlsBuurException e) {
		    lblFoutMelding
			    .setText(Taal.getString(taal, "FoutmeldingSpelbordOngeldigeZetAlleenEigenSteenAlsBuur"));
		    mediaPlayerError.seek(mediaPlayerError.getStartTime());
		    mediaPlayerError.play();
		}
		event.setDropCompleted(success);
		event.consume();

		mediaPlayerFileDrop.seek(mediaPlayerFileDrop.getStartTime());
		mediaPlayerFileDrop.play();
	    }
	});
    }

    /**
     * Deze methode zorgt ervoor dat een GUI element niet meer dropbaar is. Op dit
     * element kunnen na aanroep van deze methode geen andere GUI elementen meer
     * gedropt worden
     * 
     * @param kol Kolom op het bord
     * @param rij Rij op het bord
     */
    private void stopDropbaar(int kol, int rij) {
	vakken[kol][rij].setOnDragOver(new EventHandler<DragEvent>() {
	    @Override
	    public void handle(DragEvent event) {
		event.acceptTransferModes(TransferMode.NONE);
		event.consume();
	    }
	});
    }

    /**
     * Deze methode maakt een GUI element versleepbaar. Deze kan dan gedropt worden
     * op een ander GUI element als dit element dropbaar is.
     * 
     * @param dobbelsteen  Te verslepen element
     * @param spelerstenen Overlappend element waarin het te verslepen element zich
     *                     bevindt
     */
    public void maakDragbaar(ImageView dobbelsteen, Pane spelerstenen) {
	dobbelsteen.setOnDragDetected(new EventHandler<MouseEvent>() {
	    @Override
	    public void handle(MouseEvent event) {
		clearTextFields();
		Dragboard db = dobbelsteen.startDragAndDrop(TransferMode.ANY);
		ClipboardContent content = new ClipboardContent();
		content.putImage(dobbelsteen.getImage());
		content.putString(dobbelsteen.getId());
		db.setContent(content);
		dobbelsteen.setVisible(false);
		event.consume();
	    }
	});

	dobbelsteen.setOnDragDone(new EventHandler<DragEvent>() {
	    @Override
	    public void handle(DragEvent event) {
		if (event.getTransferMode() == TransferMode.MOVE) {
		    spelerstenen.getChildren().remove(dobbelsteen);
		} else
		    dobbelsteen.setVisible(true);
		event.consume();
	    }
	});
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
	    Parent root = loader.load();
	    SelecteerSpelerController selecteerSpelerController = loader.getController();
	    selecteerSpelerController.setDomeinController(dc);
	    selecteerSpelerController.setTaal(taal);
	    selecteerSpelerController.initScherm();
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Instellingen scherm
     * 
     * @param event
     */
    public void veranderSchermInstellingen(ActionEvent event) {

	List<String> bordGeschiedenis = new ArrayList<String>();

	for (int kol = 0; kol < BORDPATROON.length; kol++) {
	    for (int rij = 0; rij < BORDPATROON.length; rij++) {
		if (!vakken[kol][rij].getId().contains("="))
		    bordGeschiedenis.add(vakken[kol][rij].getId());
	    }
	}

	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("instellingen.fxml"));
	    Parent root = loader.load();
	    InstellingenController instellingenController = loader.getController();
	    instellingenController.setDomeinController(dc);
	    instellingenController.setTaal(taal);
	    instellingenController.initScherm();
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Laat toe een actionevent te binden aan een gui element om te switchen naar
     * het Einde Spel scherm
     * 
     * @param event
     */
    public void veranderSchermEindeSpel(ActionEvent event) {
	try {
	    FXMLLoader loader = new FXMLLoader(getClass().getResource("eindscherm.fxml"));
	    Parent root = loader.load();
	    EindschermController eindSchermController = loader.getController();
	    eindSchermController.setDomeinController(dc);
	    eindSchermController.setTaal(taal);
	    eindSchermController.initScherm();
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    Scene scene = new Scene(root);
	    stage.setScene(scene);
	    stage.show();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void stopSpel(ActionEvent event) {
	dc.stopSpel();
	veranderSchermEindeSpel(event);
    }

    /**
     * Implentatie van de methode propertyChange van de interface
     * PropertyChangeListener. Er wordt onderscheid gemaakt tussen een spelbord
     * change of een scoreblad change
     */
    @SuppressWarnings({ "exports", "unchecked" })
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
	if (evt.getPropertyName().equals("Spelbord")) {

	    List<Integer> values = (ArrayList<Integer>) evt.getNewValue();
	    vakken[values.get(1)][values.get(0)]
		    .setImage(new Image(getClass().getResourceAsStream("/Pictures/" + values.get(2) + ".png")));
	}

	if (evt.getPropertyName().equals("Scoreblad")) {
	    String values = (String) evt.getNewValue();
	    tableData.clear();
	    String[] scoresOpEenLijn = values.split("\n");
	    for (String lijn : scoresOpEenLijn) {
		String[] token = lijn.split("#");
		tableData.add(new TableScoreEntry(Boolean.parseBoolean(token[0]), Integer.parseInt(token[1]),
			Integer.parseInt(token[2]), Integer.parseInt(token[3]), Integer.parseInt(token[4]), ""));
		tbvScoreblad.setItems(tableData);
	    }
	}
    }

    /**
     * 
     * @author Wim Dhaenens
     *
     */
    public static class TableScoreEntry {

	private final String dubbel;
	private final String tien;
	private final String elf;
	private final String twaalf;
	private final int bonus;
	private final String totaal;

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
		String scoreEntry6) {

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
	public String getTotaal() {
	    return totaal;
	}
    }
}