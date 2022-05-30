package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public class ProfileController {

	@FXML Label name;
	@FXML Label mail;
	@FXML TextArea customData;
	@FXML ImageView customProfileImg;
	@FXML TextField ProfileImg;

	
	
	public void initialize() throws SQLException, URISyntaxException, IOException {
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
			}
			/*else {
				Image pimage = new Image(customImageRS.getString(1));
				customProfileImg.setImage(pimage);
				
			}*/
		}
	
		

	}

	
		public void toEdit(ActionEvent event) throws IOException {
			String sFTP = "92.178.96.124";
			String sUser = "ftpuser";
			String sPassword = "saurus12";
			//toEdit(event);
			try {
				System.out.println("Editant foto");
				Main.writeToLogFile("Loading login page");
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Tria una imatge de perfil");
				Window stage = null;
				File imgFile = fileChooser.showOpenDialog(stage);
				if(imgFile!=null) {
					Image image = new Image(imgFile.getAbsolutePath());
					System.out.println(imgFile.getAbsolutePath());
					customProfileImg.setImage(image);
					Connection myConnection=null;
					FTPClient client = new FTPClient();
					try {
						client.connect(sFTP);
						boolean login = client.login(sUser, sPassword);
						
						client.setFileType(FTP.BINARY_FILE_TYPE);
			            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			            client.enterLocalPassiveMode();
			            
						String absolutePath = imgFile.getAbsolutePath();
						FileInputStream ftp = new FileInputStream(absolutePath);
						//client.changeWorkingDirectory("//testdir");
						System.out.println(client.printWorkingDirectory());
					
						client.storeFile(absolutePath, ftp);
						
						myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306","judit","1234");
						Statement myStatement = myConnection.createStatement();
						System.out.println("UPDATE HomeIn.User SET ProfileImage = 'http://92.178.96.124/ftpuser/" + absolutePath + "' WHERE UserId = "+Contoller.getUID());
						//String str = "UPDATE HomeIn.User SET ProfileImage = 'http://92.178.96.124/ftpuser/" + absolutePath + "' WHERE UserId = "+Contoller.getUID();
						myStatement.executeUpdate( "UPDATE HomeIn.User SET ProfileImage = 'http://92.178.96.124/ftpuser/" + absolutePath.replaceAll("\\", "\\") + "' WHERE UserId = "+Contoller.getUID());
						
					}catch(IOException ioe) {
						ioe.printStackTrace();
					}
					
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
}
