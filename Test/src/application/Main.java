package application;
	
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	static FileWriter logFile;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Set title
			primaryStage.setTitle("Prototype");
			//Set not resizable
			primaryStage.setResizable(false);
			//Load Fxml's, for test purposes
			Parent loginScreen = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));

			//Create Scenes
			Scene loginScreenScene = new Scene(loginScreen,1280,720);
			
			
			
			//Set scene
			primaryStage.setScene(loginScreenScene);
			
			//Show stage
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)   {
		try {
			logFile = new FileWriter("./log.txt");
			System.out.println(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
		
		
	}
	public static void writeToLogFile(String text) throws IOException {
		logFile.write(text+"\n");
		logFile.flush();
	}
}
