/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	this.txtResult.clear();
    	if(!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, crea grafo");
    		return;
    	}
    	Actor a = this.boxAttore.getValue();
    	if (a==null) {
    		this.txtResult.setText("Errore, seleziona un attore");
    		return;
    	}
    	this.txtResult.setText("Attori raggiungibili:\n");
    	List<Actor> raggiungibili= model.raggiungibili(a);
    	for (Actor act : raggiungibili){
    		this.txtResult.appendText(act.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	String genere=this.boxGenere.getValue();
    	if (genere==null) {
    		this.txtResult.setText("Errore, selezionere un genere");
    		return;
    	}
    	model.creaGrafo(genere);
    	this.txtResult.setText("GRAFO CREATO");
    	this.txtResult.appendText("\n#Vertici: "+model.getNumVertici());
    	this.txtResult.appendText("\n#Archi: "+model.getNumArchi());
    	this.boxAttore.getItems().clear();
    	this.boxAttore.getItems().addAll(model.getVertici());
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	this.txtResult.clear();
    	if(!model.isGrafoCreato()) {
    		this.txtResult.setText("Errore, crea grafo");
    		return;
    	}
    	String genere=this.boxGenere.getValue();
    	if (genere==null) {
    		this.txtResult.setText("Errore, selezionere un genere");
    		return;
    	}
    	String nString=this.txtGiorni.getText();
    	int n;
    	try {
    		n= Integer.parseInt(nString);
    	} catch(NumberFormatException e ) {
    		this.txtResult.setText("Errore, inserire un numero di giorni");
    		return;
    	}
    	model.simula(n, genere);
    	this.txtResult.setText("Sono stati intervistati "+model.numAttoriIntervistati()+" attori\n");
    	this.txtResult.appendText("e sono state effettuate "+model.numPause()+" pause\n");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(model.getAllGenres());
    }
}
