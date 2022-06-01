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
import javafx.scene.input.MouseEvent;
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
 * @author Judits
 *
 */
public class Contoller {

	//Set elements to use from fxml
	//Buttons
	@FXML private Button homeButton = new Button();
	@FXML private Button profileButton = new Button();

	//Menu buttons for filters
	@FXML MenuButton testFilters = new MenuButton();
	@FXML MenuButton zonesBtn;
	@FXML MenuButton citysBtn;

	//Labels
	@FXML Label noResults;

	//Panes
	@FXML BorderPane mainPagePane = new BorderPane();
	@FXML HBox mainPageHBox;
	@FXML ScrollPane mainPageSPane;

	//Other vars

	//Scroll
	private int pos = 0;
	private final int minPos = 0;
	private final int maxPos = 100;
	//To check if the user has logged
	public static boolean hasLogged;
	private static int uid;

	//TO switch scene
	private Stage stage;
	private Scene scene;
	private Parent root;
	//Filters
	private static boolean rental;
	private static boolean parking;
	private static boolean elevator;
	private static String zone;
	private static String city;
	private static int rooms;
	private static int bathRooms;
	private static int maxPrice = 0;
	private static String[] toReturn;


	//Init method
	/**
	 * Javafx init method for the Main page controller
	 * @throws URISyntaxException
	 * @throws SQLException
	 * @throws IOException
	 */
	public void initialize() throws URISyntaxException, SQLException, IOException {
		Main.writeToLogFile("Entered Main page");

		noResults.setText("");
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

		//Change main page scroll to horizontal scroll
		mainPageHBox.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				mainPageSPane.setHvalue(mainPageSPane.getHvalue() - event.getDeltaY() / mainPageSPane.getWidth() * 3 );
			}
		});
		Main.writeToLogFile("Adding results to main page");

		//Add main page propertys to the pane
		for(int i = 0; i < 5; i++) {
			mainPageHBox.getChildren().add(mainPageResults(i));
			Main.writeToLogFile((i+1)+"/5");
		}
		//----------
		//Connect to DB
		Connection myConnection=null;
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
			e.printStackTrace();
		}
		Main.writeToLogFile("DB Connected");
		Statement myStatement = myConnection.createStatement();

		//Search avaliable citys
		ResultSet citysRS = myStatement.executeQuery("SELECT City FROM HomeIn.Property Group By City");
		Main.writeToLogFile("Init Filters");
		ArrayList<MenuItem> citysRSAR = new ArrayList(); 
		//Add city options to the filter menu
		//First to the arraylist
		while(citysRS.next()) {
			citysRSAR.add(new MenuItem(citysRS.getString(1)));

		}
		//Init the button
		citysBtn.hide();
		citysBtn.show();
		//Set events for the items on the arraylist and add them to the menu
		for(MenuItem mi : citysRSAR) {
			citysBtn.getItems().add(mi);
			mi.setOnAction((event) -> { 
				city = ((MenuItem)event.getSource()).getText();
				System.out.println(city);
			});
		}
		//Do the same for the zones
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


	/**
	 * Method to add the propertires to the main page
	 * @param i
	 * @return
	 * @throws SQLException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public VBox mainPageResults(int i) throws SQLException, URISyntaxException, IOException{
		//Get the string with price, city and image
		String test [][] = searchMainPageResults();
		//Create new VBox
		VBox newVBox = new VBox();
		Main.writeToLogFile("Setting Image");
		//Load image from repo
		Image homeBtnImage = new Image(test[2][i]);
		ImageView homeBtnImageView = new ImageView(homeBtnImage);
		homeBtnImageView.setPickOnBounds(true);
		homeBtnImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				PropertyViewController.pToShow = Integer.parseInt(test[3][i]);
				try {
					root = FXMLLoader.load(getClass().getResource("ShowProperty.fxml"));
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
		//Set image size
		homeBtnImageView.setFitWidth(360);
		homeBtnImageView.setFitHeight(360);
		//add the image to the VBox
		newVBox.getChildren().add(homeBtnImageView);
		Main.writeToLogFile("Setting label");
		//Add labels with the text to the Vbox
		newVBox.getChildren().add(new Label(test[0][i] + "€ " + test[1][i] ));
		newVBox.setAlignment(Pos.CENTER);
		return newVBox;
	}

	public void test(ActionEvent E) {	
		System.out.println("e");
	}
	/**
	 * Method returns a 2D array with Price,City and an Imgage from the propertyres table
	 * @return 
	 * @throws SQLException
	 * @throws IOException
	 */
	public String[][] searchMainPageResults() throws SQLException, IOException {
		//Create connection
		Connection myConnection=null;
		try {
			System.out.println("ControllerDB");
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");			
			e.printStackTrace();

		}
		Main.writeToLogFile("DB Connected");
		Statement myStatement = myConnection.createStatement();

		//Execute query
		ResultSet myResSet = myStatement.executeQuery("SELECT Price,City,PropertyId FROM HomeIn.Property");

		//Save price
		ArrayList<String> priceAR = new ArrayList<String>();
		//City
		ArrayList<String> cityAR= new ArrayList<String>();
		//Id
		ArrayList<String> propertysAR= new ArrayList<String>();
		//Image link
		ArrayList<String> imagesAR= new ArrayList<String>();
		Main.writeToLogFile("Getting propertires from DB");
		while(myResSet.next() ) {
			priceAR.add(myResSet.getString(1));
			cityAR.add(myResSet.getString(2));
			propertysAR.add(myResSet.getString(3));
			

		}

		//Number of results to show
		int length = priceAR.size();
		if(length > 5) length = 5;
		String toReturn[][] = new String [4][length];
		//To array
		toReturn[0] = priceAR.toArray(new String[length]);
		toReturn[1] = cityAR.toArray(new String[length]);
		toReturn[3] = propertysAR.toArray(new String[length]);
		//Add all ids to a var to get the images corresponding to the propertys
		String allIds = "";
		for(int i = 0; i<length;i++) {
			allIds +=propertysAR.get(i);
			if(i < length-1) allIds+=",";
		}
		//Execute querty
		ResultSet myResSet2 = myStatement.executeQuery("SELECT ImageLink FROM HomeIn.ImageList"
				+" WHERE PropertyId IN(" + allIds + ") Group By PropertyId");
		//Add images to the array
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

	/**
	 * Loafs Profile page
	 * @param event
	 * @throws IOException
	 */
	public void toProfile(ActionEvent event) throws IOException {
		//Load Fxml
		if(isHasLogged()) {
			root = FXMLLoader.load(getClass().getResource("ProfilePage.fxml"));
		}else {
			root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
		}
		//Set stage and scenetrue
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		//Show stage
		stage.show();
	}


	/**
	 * Sets the rental filter based on a menu
	 * @param E
	 */
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
	/**
	 * Sets the elevator filter based on a menu
	 * @param E
	 */
	public void setEleator(ActionEvent E) {
		String bName = ((MenuItem)E.getSource()).getText();
		System.out.println(bName);
		if(bName.equals("Si")) {
			Contoller.elevator = true;
		}else {
			Contoller.elevator = false;
		}
		System.out.println(Contoller.elevator);
	}

	/**
	 * Sets the parking filter based on a menu
	 * @param E
	 */
	public void setPrking(ActionEvent E) {
		String bName = ((MenuItem)E.getSource()).getText();
		System.out.println(bName);
		if(bName.equals("Si")) {
			Contoller.parking = true;
		}else {
			Contoller.parking = false;
		}
		System.out.println(Contoller.parking);
	}

	/**
	 * Sets the rooms filter based on a menu
	 * @param E
	 */
	public void setRooms(ActionEvent E) {
		String bName = ((MenuItem)E.getSource()).getText();
		System.out.println(bName);
		if(bName.equals("1")) {
			Contoller.rooms = 1;
		}else if(bName.equals("2")) {
			Contoller.rooms = 1;
		}else {
			Contoller.rooms = 3;
		}

		System.out.println(Contoller.parking);
	}
	/**
	 * Sets the bathrooms filter based on a menu
	 * @param E
	 */
	public void setBathrooms(ActionEvent E) {
		String bName = ((MenuItem)E.getSource()).getText();
		System.out.println(bName);
		if(bName.equals("1")) {
			Contoller.bathRooms = 1;
		}else if(bName.equals("2")) {
			Contoller.bathRooms = 1;
		}else {
			Contoller.bathRooms = 3;
		}

		System.out.println(Contoller.bathRooms);
	}

	/**
	 * Sets the price filter based on a menu
	 * @param E
	 */
	public void setPrice(ActionEvent E) {
		String bName = ((MenuItem)E.getSource()).getText();
		System.out.println(bName);
		switch(bName) {
		case "500": Contoller.maxPrice = Integer.parseInt(bName); break;
		case "1000": Contoller.maxPrice = Integer.parseInt(bName); break;
		case "2000": Contoller.maxPrice = Integer.parseInt(bName); break;
		case "50000": Contoller.maxPrice = Integer.parseInt(bName); break;
		case "150000": Contoller.maxPrice = Integer.parseInt(bName); break;
		case "500000": Contoller.maxPrice = Integer.parseInt(bName); break;
		case "1000000": Contoller.maxPrice = Integer.parseInt(bName); break;
		}

		System.out.println(Contoller.maxPrice);
	}
	/**
	 * Method to search based on filters
	 * @param E
	 * @throws SQLException
	 * @throws IOException 
	 */
	public void search(ActionEvent event) throws SQLException, IOException {
		String whereFilters="";
		if(rental) {
			whereFilters+=" Rental Like '1' AND ";	
		}
		if(parking) {
			whereFilters+=" Parking Like '1' AND ";	
		}
		if(elevator) {
			whereFilters+=" Elevator Like '1' AND ";	
		}
		if(zone != null) {
			whereFilters+=" Zone Like '" + zone + "' AND ";
		}
		if(city != null) {
			whereFilters+=" City Like '" + city + "' AND ";
		}
		switch(rooms) {
		case 1: 
			whereFilters+=" Rooms Like '1' AND ";	
			break;
		case 2: 
			whereFilters+=" Rooms Like '2' AND ";	
			break;
		case 3: 
			whereFilters+=" Rooms > '2' AND ";	
			break;
		}
		switch(bathRooms) {
		case 1: 
			whereFilters+=" Bathrooms Like '1' AND ";	
			break;
		case 2: 
			whereFilters+=" Bathrooms Like '2' AND ";	
			break;
		case 3: 
			whereFilters+=" Bathrooms > '2' AND ";	
			break;
		}

		if(maxPrice != 0) {
			whereFilters+=" Price < '" + maxPrice + "' AND ";
		}
		//
		Connection myConnection=null;
		try {
			System.out.println("ControllerDB");
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Statement myStatement = myConnection.createStatement();

		ResultSet resultsRs = myStatement .executeQuery("Select PropertyId From HomeIn.Property WHERE " + whereFilters + " '1' LIKE '1'");
		System.out.println("Select PropertyId From HomeIn.Property WHERE " + whereFilters + " '1' LIKE '1'");

		if(resultsRs.isBeforeFirst()) {
			ArrayList<String> pIds = new ArrayList();
			while(resultsRs.next()) {
				pIds.add(resultsRs.getString(1));
			}
			toReturn = pIds.toArray(new String [pIds.size()]);
			System.out.println("EEE");
			Main.writeToLogFile("Loaging register page");
			root = FXMLLoader.load(getClass().getResource("PropertyScreen.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}else {
			noResults.setText("Sensse resultats!");
			toReturn = null;
		}

	}

	public static String[] avaliablePtrs() {
		return toReturn;
	}


	public static boolean hasElevator() {
		return elevator;
	}
	public static boolean isRental() {
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
