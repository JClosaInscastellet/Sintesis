package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PropertyViewController {
	//TO switch scene
	private Stage stage;
	private Scene scene;
	private Parent root;
	public static int pToShow;
	
	@FXML Label room;
	@FXML Label bathroom;
	@FXML Label surface;
	@FXML Label price;
	@FXML Label city;
	@FXML Label zone;
	@FXML Label parking;
	@FXML Label elevator;
	
	
	public void pullBack(ActionEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
