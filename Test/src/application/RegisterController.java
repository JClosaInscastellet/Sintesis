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

public class RegisterController {
	private String name;
	private String surname;
	private String mail;
	private String city;
	private String password;

	@FXML TextField nameTField;
	@FXML TextField mailTField;
	@FXML TextField cityTField;
	@FXML TextField surnameTField;
	@FXML PasswordField passwordPField;
	@FXML Button goLogin;
	@FXML Button checkRegister;
	@FXML Label errorUserExists;
	//--
	private Stage stage;
	private Scene scene;
	private Parent root;
//

	public void checkAccount(ActionEvent event) throws SQLException, IOException {
		name = nameTField.getText();
		surname = surnameTField.getText();
		mail = mailTField.getText();
		city = cityTField.getText();
		password = passwordPField.getText();
		boolean canRegister = true;
		String maxUID = null;

		Connection myConnection=null;
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://192.168.1.99:3306","judit","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
			System.out.println("ee");
			e.printStackTrace();

		}

		Statement myStatement = myConnection.createStatement();
		ResultSet lastUID = myStatement.executeQuery("SELECT MAX(UserId) FROM HomeIn.User");
		while(lastUID.next()) {
			maxUID = lastUID.getString(1); 
		}
		maxUID = Integer.toString(Integer.parseInt(maxUID)+1); 
		ResultSet userExists = myStatement.executeQuery("SELECT Mail FROM HomeIn.User");
		while(userExists.next()) {
			if(userExists.getString(1).equals(mail) ) {
				canRegister = false;
			}
		}
		if(!canRegister) {
			errorUserExists.setText("User already exists!");
		}else {
			myStatement.executeUpdate("INSERT INTO HomeIn.User (UserId,Name,LastName,Mail,City,Password) VALUES('"
					+maxUID+"', '"+name+"', '"+surname+"', '"+mail+"', '"+city+"', '"+password+"')");
			toLogin(event);
		}

	}



	//Switch to main page
	public void toLogin(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}


}
