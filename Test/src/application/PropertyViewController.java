package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
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
	@FXML VBox images;



	public void initialize() throws SQLException {
		Connection myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");
		Statement myStatement = myConnection.createStatement();
		ResultSet allElements = myStatement.executeQuery("SELECT * FROM HomeIn.Property Where PropertyId Like "+pToShow);
		while(allElements.next()) {
			price.setText(price.getText() + " " + allElements.getString(2));
			surface.setText(surface.getText() + " " + allElements.getString(3));
			room.setText(room.getText() + " " + allElements.getString(4));
			bathroom.setText(bathroom.getText() + " " + allElements.getString(5));
			parking.setText(parking.getText() + " " + allElements.getString(8));
			elevator.setText(elevator.getText() + " " + allElements.getString(10));
			city.setText(city.getText() + " " + allElements.getString(11));
			zone.setText(zone.getText() + " " + allElements.getString(13));
		}
		
		images.setSpacing(20);
		ResultSet allImages  = myStatement.executeQuery("SELECT * FROM HomeIn.ImageList Where PropertyId Like "+pToShow);
		System.out.println("SELECT * FROM HomeIn.ImageList Where PropertyId Like "+pToShow);
		ArrayList<Image> imagesToAdd = new ArrayList();
		ArrayList<ImageView> imageViewsToAdd = new ArrayList();
		int i = 0;
		images.setAlignment(Pos.CENTER);
		while(allImages.next()) {
			System.out.println(allImages.getString(2));
			imagesToAdd.add(new Image(allImages.getString(2)));
			imageViewsToAdd.add(new ImageView(imagesToAdd.get(i)));
			imageViewsToAdd.get(i).setFitHeight(700);
			imageViewsToAdd.get(i).setFitWidth(700);
			images.getChildren().add(imageViewsToAdd.get(i));    
			i++;
		}
		
	}

	public void pullBack(ActionEvent event) throws IOException {

		root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
