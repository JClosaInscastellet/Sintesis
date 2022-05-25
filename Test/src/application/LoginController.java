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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

	@FXML Button noLogin;
	@FXML TextField user;
	@FXML PasswordField password;
	private Stage stage;
	private Scene scene;
	private Parent root;
	private boolean loged = false;
	private String userStr;
	private String passStr;
	

	
	public void initialize() {

	}

	public void checkUser(ActionEvent event) throws SQLException, IOException {
		boolean canLogin = false;
		Connection myConnection=null;
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://192.168.1.99:3306","judit","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
			System.out.println("ee");
			e.printStackTrace();

		}
		Statement myStatement = myConnection.createStatement();
		ResultSet userPassword = myStatement.executeQuery("SELECT Mail,Password FROM HomeIn.User");
		
		userStr = user.getText();
		passStr = password.getText();
		System.out.println(userStr +"+"+passStr);
		
		while(userPassword.next()) {
			if(userPassword.getString(1).equals(userStr) && userPassword.getString(2).equals(passStr)) {
				canLogin = true;
				System.out.println("Eureca");
			}
		}
		if(canLogin==true) {
			loged = true;
			toMainPage(event);
		}
		System.out.println("Clicked Login Button");
	}




	//Switch to main page
	public void toMainPage(ActionEvent event) throws IOException {
		Contoller.setHasLogged(loged);
		root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
