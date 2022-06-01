package application;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResultsController {

	//To switch scenes
	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML HBox searchPageHBox;
	@FXML ScrollPane searchPageSP;
	private String foundPropertys[] = Contoller.avaliablePtrs(); 
	private String propertysElementsStr[][];
	public void initialize() throws IOException, SQLException, URISyntaxException {
		searchPageHBox.setSpacing(20); 
		//Change main page scroll to horizontal scroll
		searchPageHBox.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				searchPageSP.setHvalue(searchPageSP.getHvalue() - event.getDeltaY() / searchPageSP.getWidth() * 3 );
			}
		});

		propertysElements();
		for(int i = 0; i < propertysElementsStr[0].length; i++) {
			searchPageHBox.getChildren().add(addElements(i));
		}



	}
	/**
	 * 
	 * @param i
	 * @return
	 */
	public VBox addElements(int i) {
		VBox newVBox = new VBox();

		Image pImage = new Image(propertysElementsStr[2][i]);
		ImageView pImageView = new ImageView(pImage);
		pImageView.setPickOnBounds(true);
		pImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				PropertyViewController.pToShow = Integer.parseInt(foundPropertys[i]);
				try {
					root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage = (Stage)((Node)event.getSource()).getScene().getWindow();
				scene = new Scene(root);
				stage.setScene(scene);
				stage.show();
				System.out.println(PropertyViewController.pToShow);
			}
		});
		pImageView.setFitWidth(360);
		pImageView.setFitHeight(360);
		newVBox.getChildren().add(pImageView);
		newVBox.getChildren().add(new Label(propertysElementsStr[0][i] + "€ " + propertysElementsStr[1][i] ));
		newVBox.setAlignment(Pos.CENTER);

		return newVBox;
	}


	public void propertysElements() throws SQLException{
		ArrayList<String> images = new ArrayList();
		ArrayList<String> price = new ArrayList();
		ArrayList<String> location = new ArrayList();
		String allPIds ="";
		for(int i = 0; i<foundPropertys.length;i++) {
			allPIds +=foundPropertys[i];
			if(i < foundPropertys.length-1) allPIds+=",";
		}
		Connection myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");
		Statement myStatement = myConnection.createStatement();
		ResultSet priceLoc = myStatement.executeQuery("SELECT Price,City "
				+ "FROM HomeIn.Property Where PropertyId In(" +allPIds +")");

		while(priceLoc.next()) {
			price.add(priceLoc.getString(1));
			location.add(priceLoc.getString(2));
		}

		ResultSet imagesRS = myStatement.executeQuery("SELECT ImageLink "
				+ "FROM HomeIn.ImageList Where PropertyId In(" +allPIds +") Group By PropertyId");
		while(imagesRS.next()) {
			images.add(imagesRS.getString(1));
		}
		String toReturn[][] = new String [3][foundPropertys.length];
		toReturn[0] = price.toArray(new String[foundPropertys.length]);
		toReturn[1] = location.toArray(new String[foundPropertys.length]);
		toReturn[2] = images.toArray(new String[foundPropertys.length]);
		propertysElementsStr = toReturn;
	}

	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void toMainPage(ActionEvent event) throws IOException {
		//Like the controller class but setting or not the UID on the main Controller if the user is loged/not loged
		Main.writeToLogFile("Loading Main page");
		root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

}



