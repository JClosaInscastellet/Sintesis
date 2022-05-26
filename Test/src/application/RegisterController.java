package application;

import java.io.IOException;

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
	//--
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	
	
	
	
	//Switch to main page
	public void toLogin(ActionEvent event) throws IOException {
		root = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	
}
