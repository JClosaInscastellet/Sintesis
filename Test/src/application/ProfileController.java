package application;

import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProfileController {

	@FXML Label name;
	@FXML Label mail;
	@FXML TextArea customData;
	@FXML ImageView customProfileImg;

	public void initialize() throws SQLException, URISyntaxException {
		//Get user Info
		Connection myConnection=null;
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
			System.out.println("ee");
			e.printStackTrace();

		}
		Statement myStatement = myConnection.createStatement();
		ResultSet nameSurname = myStatement.executeQuery("SELECT Name,LastName,Mail FROM HomeIn.User WHERE UserId = "+Contoller.getUID());

		while(nameSurname.next()) {
			name.setText(nameSurname.getString(1) + " " +nameSurname.getString(2));
			mail.setText(nameSurname.getString(3));
		}
		ResultSet customDataRS = myStatement.executeQuery("SELECT CustomData FROM HomeIn.User WHERE UserId = "+Contoller.getUID());

		if(customDataRS.next()) {
			customData.setPromptText(customDataRS.getString(1));
		}else {
			customData.setPromptText("");
		}

		ResultSet customImageRS = myStatement.executeQuery("SELECT ProfileImage FROM HomeIn.User WHERE UserId = "+Contoller.getUID());
		System.out.println("SELECT ProfileImage FROM HomeIn.User WHERE UserId = "+Contoller.getUID());
		while(customImageRS.next()) {
			if(customImageRS.getString(1) == null) {
				Image pimage = new Image("http://92.178.96.124/ftpuser/avatar.png");
				customProfileImg.setImage(pimage);
			}else {
				Image pimage = new Image(customImageRS.getString(1));
				customProfileImg.setImage(pimage);
			}
		}


	}

}
