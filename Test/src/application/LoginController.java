package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	@FXML Button noLogin;
	@FXML Button goRegister;
	@FXML TextField user;
	@FXML PasswordField password;
	@FXML Label incorrectLogin;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean loged = false;
	private String userStr;
	private String passStr;
	private int uidInt;
	

	
	public void initialize() throws IOException {
		Main.writeToLogFile("Application Started");
	}

	public void checkUser(ActionEvent event) throws SQLException, IOException {
		Main.writeToLogFile("Login started");
		boolean canLogin = false;
		Connection myConnection=null;
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
			e.printStackTrace();

		}
		Main.writeToLogFile("DB Connected");
		Statement myStatement = myConnection.createStatement();
		ResultSet userPassword = myStatement.executeQuery("SELECT Mail,Password FROM HomeIn.User");
		
		userStr = user.getText();
		passStr = password.getText();
		System.out.println(userStr +"+"+passStr);
		Main.writeToLogFile("Checking credentials for: ");
		Main.writeToLogFile("User: userStr");
		Main.writeToLogFile("Password:"+ passStr);


		while(userPassword.next()) {
			if(userPassword.getString(1).equals(userStr) && userPassword.getString(2).equals(passStr)) {
				canLogin = true;
				Main.writeToLogFile("User found!");
			}

		}

		ResultSet uid = myStatement.executeQuery("SELECT UserId From HomeIn.User WHERE Mail LIKE '"+userStr+"'");
		while(uid.next()) {
			uidInt = Integer.parseInt(uid.getString(1));
			Main.writeToLogFile("Saved UserId: "+uidInt);
		}
		
		if(canLogin==true) {
			loged = true;
			Main.writeToLogFile("Login Correct");
			toMainPage(event);
		}else {
			incorrectLogin.setText("Incorrect Mail/Password!");
			Main.writeToLogFile("Login Failed: User Not found");
		}
		
		
		System.out.println("Clicked Login Button");
	}


  

	//Switch to main page
	public void toMainPage(ActionEvent event) throws IOException {
		Main.writeToLogFile("Loading Main page");
		Contoller.setHasLogged(loged);
		if(loged) {
			Contoller.setUID(uidInt);
		}
		root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	public void toRegisterPage(ActionEvent event) throws IOException {
		Main.writeToLogFile("Loaging register page");
		root = FXMLLoader.load(getClass().getResource("RegisterScreen.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
