package application;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.net.URISyntaxException;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
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
    

	//Init method
	public void initialize() throws URISyntaxException, SQLException {

		//Home button
		Image homeBtnImage = new Image(getClass().getResource("img/icon.png").toURI().toString());
	    ImageView homeBtnImageView = new ImageView(homeBtnImage);
	    homeBtnImageView.setFitHeight(40);	    
	    homeBtnImageView.setPreserveRatio(true);
	    homeButton.setGraphic(homeBtnImageView);
	    //Profile button
	    profileButton.setText("");
	    
	    //Filters
	    //multiple
	    testFilters.setText("Various options");
	    

	    //Main Page HBox

	    String test [][] = queryTest();

	    mainPageHBox.setSpacing(20); 
	    
	    mainPageHBox.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
            	mainPageSPane.setHvalue(mainPageSPane.getHvalue() - event.getDeltaY() / mainPageSPane.getWidth() * 3 );
            }
        });
	    for(int i = 0; i < test[0].length; i++) {
	    	mainPageHBox.getChildren().add(mainPageResults(i));
	    	System.out.println("EEE");
	    }
	}
	public VBox mainPageResults(int i) throws SQLException, URISyntaxException{
		String test [][] = queryTest();
		VBox newVBox = new VBox();
		Image homeBtnImage = new Image(getClass().getResource("img/icon.png").toURI().toString());
	    ImageView homeBtnImageView = new ImageView(homeBtnImage);
		newVBox.getChildren().add(homeBtnImageView);
		newVBox.getChildren().add(new Label(test[0][i] + " \n " + test[1][i] ));
		newVBox.setAlignment(Pos.CENTER);
		return newVBox;
	}
    
	public void test(ActionEvent E) {	
		System.out.println("e");
	}
	public String[][] queryTest() throws SQLException {
		Connection myConnection=null;
		try {
			 myConnection = DriverManager.getConnection("jdbc:mysql://192.168.1.99:3306","test","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","test","1234");
			System.out.println("ee");
			e.printStackTrace();
			
		}
		Statement myStatement = myConnection.createStatement();
		
		ResultSet myResSet = myStatement.executeQuery("SELECT * FROM test.prova");
		
		
		
		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();
		while(myResSet.next() ) {
			ids.add(myResSet.getString(1));
			names.add(myResSet.getString(2));
		}
		int length = ids.size();
		if(length < 10) length = 10;
		String toReturn[][] = new String [2][length];
		toReturn[0] = ids.toArray(new String[length]);
		toReturn[1] = names.toArray(new String[length]);
		return toReturn;
	
	}
	
	//Setters/Getters
	public static boolean isHasLogged() {
		return hasLogged;
	}
	public static void setHasLogged(boolean hasLogged) {
		Contoller.hasLogged = hasLogged;
	}
	
	
}
