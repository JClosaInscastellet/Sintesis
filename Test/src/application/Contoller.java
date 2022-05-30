package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
/**
 * 
 * @author Judit Closa
 * 
 *
 */
public class Contoller {

	//Set elements to use from fxml
	//Buttons
	@FXML private Button homeButton = new Button();
	@FXML private Button profileButton = new Button();

	@FXML MenuButton testFilters = new MenuButton();

	//Panes
	@FXML BorderPane mainPagePane = new BorderPane();
	@FXML HBox mainPageHBox;
	@FXML ScrollPane mainPageSPane;

	//Other vars
	private int pos = 0;
	private final int minPos = 0;
	private final int maxPos = 100;
	//To check if the user has logged
	private static boolean hasLogged;
	private static int uid;

	//TO siwtch scene
	private Stage stage;
	private Scene scene;
	private Parent root;
	//Filters
	private static boolean rental;
	private static boolean parking;
	private static boolean elevator;
	@FXML MenuButton zonesBtn;
	@FXML MenuButton citysBtn;
	private static String zone;
	private static String city;
	private static int rooms;
	private static int bathRooms;
	private static int maxPrice;

	//Log file
	

	//Init method
	public void initialize() throws URISyntaxException, SQLException, IOException {
		Main.writeToLogFile("Entered Main page");

		//Home button
		//Import image
		Image homeBtnImage = new Image(getClass().getResource("img/homein.png").toURI().toString());
		//Create ImageView To display image
		ImageView homeBtnImageView = new ImageView(homeBtnImage);
		//Set size
		homeBtnImageView.setFitHeight(40);	    
		homeBtnImageView.setPreserveRatio(true);
		//Add image to button
		homeButton.setGraphic(homeBtnImageView);
		//Profile button
		//Remove text from profile button
		profileButton.setText("");


		mainPageHBox.setSpacing(20); 
		Main.writeToLogFile("Init horintzontal scroll");
		mainPageHBox.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				mainPageSPane.setHvalue(mainPageSPane.getHvalue() - event.getDeltaY() / mainPageSPane.getWidth() * 3 );
			}
		});
		Main.writeToLogFile("Adding results to main page");
		for(int i = 0; i < 5; i++) {
			mainPageHBox.getChildren().add(mainPageResults(i));
			Main.writeToLogFile((i+1)+"/5");
		}
		Connection myConnection=null;
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
			System.out.println("ee");
			e.printStackTrace();
		}
		Main.writeToLogFile("DB Connected");
		Statement myStatement = myConnection.createStatement();

		ResultSet citysRS = myStatement.executeQuery("SELECT City FROM HomeIn.Property Group By City");
		Main.writeToLogFile("Init Filters");
		ArrayList<MenuItem> citysRSAR = new ArrayList(); 
		while(citysRS.next()) {
			citysRSAR.add(new MenuItem(citysRS.getString(1)));

		}
		citysBtn.hide();
		citysBtn.show();
		for(MenuItem mi : citysRSAR) {
			citysBtn.getItems().add(mi);
			mi.setOnAction((event) -> { 
				city = ((MenuItem)event.getSource()).getText();
				System.out.println(city);
			});
		}
		ResultSet zonesRS = myStatement.executeQuery("SELECT Zone FROM HomeIn.Property Group By Zone");
		ArrayList<MenuItem> zonesMIAR = new ArrayList(); 
		while(zonesRS.next()) {
			zonesMIAR.add(new MenuItem(zonesRS.getString(1)));

		}
		for(MenuItem mi : zonesMIAR) {
			zonesBtn.getItems().add(mi);
			mi.setOnAction((event) -> { 
				zone = ((MenuItem)event.getSource()).getText();
				System.out.println(zone);
			});
		}
	}

	public VBox mainPageResults(int i) throws SQLException, URISyntaxException, IOException{
		String test [][] = queryTest();
		VBox newVBox = new VBox();
		Main.writeToLogFile("Setting Image");
		Image homeBtnImage = new Image(test[2][i]);
		ImageView homeBtnImageView = new ImageView(homeBtnImage);
		homeBtnImageView.setFitWidth(360);
		homeBtnImageView.setFitHeight(360);
		newVBox.getChildren().add(homeBtnImageView);
		Main.writeToLogFile("Setting label");
		newVBox.getChildren().add(new Label(test[0][i] + "€	" + test[1][i] ));
		newVBox.setAlignment(Pos.CENTER);
		return newVBox;
	}

	public void test(ActionEvent E) {	
		System.out.println("e");
	}
	public String[][] queryTest() throws SQLException, IOException {
		Connection myConnection=null;
		try {
			System.out.println("ControllerDB");
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");			e.printStackTrace();

		}
		Main.writeToLogFile("DB Connected");
		Statement myStatement = myConnection.createStatement();

		ResultSet myResSet = myStatement.executeQuery("SELECT Price,City,PropertyId FROM HomeIn.Property");
		


		ArrayList<String> priceAR = new ArrayList<String>();
		ArrayList<String> cityAR= new ArrayList<String>();
		ArrayList<String> propertysAR= new ArrayList<String>();
		ArrayList<String> imagesAR= new ArrayList<String>();
		Main.writeToLogFile("Getting propertires from DB");
		while(myResSet.next() ) {
			priceAR.add(myResSet.getString(1));
			cityAR.add(myResSet.getString(2));
			propertysAR.add(myResSet.getString(3));

		}

		int length = priceAR.size();
		if(length > 5) length = 5;
		String toReturn[][] = new String [3][length];
		toReturn[0] = priceAR.toArray(new String[length]);
		toReturn[1] = cityAR.toArray(new String[length]);

		String allIds = "";
		for(int i = 0; i<length;i++) {
			allIds +=propertysAR.get(i);
			if(i < length-1) allIds+=",";
		}
		ResultSet myResSet2 = myStatement.executeQuery("SELECT ImageLink FROM HomeIn.ImageList"
				+" WHERE PropertyId IN(" + allIds + ") Group By PropertyId");
		while(myResSet2.next()) {
			imagesAR.add(myResSet2.getString(1));
		}
		toReturn[2] = imagesAR.toArray(new String[length]);
		Main.writeToLogFile("Connection Succesfull");
		return toReturn;

	}

	//Setters/Getters
	public static boolean isHasLogged() {
		return hasLogged;
	}
	public static void setHasLogged(boolean hasLogged) {
		Contoller.hasLogged = hasLogged;
	}
	public static void setUID(int uid) {
		Contoller.uid = uid;
	}
	public static int getUID() {
		return uid;
	}

	public void toProfile(ActionEvent event) throws IOException {
		if(isHasLogged()) {
			root = FXMLLoader.load(getClass().getResource("ProfilePage.fxml"));
		}else {
			root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
		}

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}



	public void setRental(ActionEvent E) {
		String bName = ((MenuItem)E.getSource()).getText();
		System.out.println(bName);
		if(bName.equals("Si")) {
			Contoller.rental = true;
		}else {
			Contoller.rental = false;
		}
		System.out.println(Contoller.rental);
	}


	public boolean isRental() {
		return rental;
	}
	public static void setRental(boolean rental) {
		Contoller.rental = rental;
	}
	public String getZone() {
		return zone;
	}
	public  void setZone(ActionEvent E) throws SQLException {
		//zonesBtn
	}
}
