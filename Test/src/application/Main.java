package application;
	
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * Main class for this software
 * @author Judits
 *
 */
public class Main extends Application {
	
	//Log file
	private static FileWriter logFile;
	
	/**
	 * Javafx start method
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			//Set title
			primaryStage.setTitle("Prototype");
			//Set not resizable
			primaryStage.setResizable(false);
			//Load Fxml
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
	
	/**
	 * Main metod
	 * @param args
	 */
	public static void main(String[] args)   {
		try {
			//Init log file
			logFile = new FileWriter("./log.txt");
			System.out.println(logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Launch app
		launch(args);
		
		
	}
	
	/**
	 * Metod to write to the log file
	 * @param text Text to write
	 * @throws IOException
	 */
	public static void writeToLogFile(String text) throws IOException {
		logFile.write(text+"\n");
		logFile.flush();
	}
}
