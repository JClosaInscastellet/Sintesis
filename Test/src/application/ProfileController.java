package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ProfileController {

	@FXML
	Label name;
	@FXML
	Label mail;
	@FXML
	TextArea customData;
	@FXML
	ImageView customProfileImg;
	@FXML
	TextField ProfileImg;
	@FXML 
	Button backButton;

	public void initialize() throws SQLException, URISyntaxException, IOException {
		// Get user Info
		Connection myConnection = null;
		try {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306", "judit", "1234");
		} catch (SQLException e) {
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306", "judit", "1234");
			System.out.println("ee");
			e.printStackTrace();

		}
		Statement myStatement = myConnection.createStatement();
		ResultSet nameSurname = myStatement
				.executeQuery("SELECT Name,LastName,Mail FROM HomeIn.User WHERE UserId = " + Contoller.getUID());

		while (nameSurname.next()) {
			name.setText(nameSurname.getString(1) + " " + nameSurname.getString(2));
			mail.setText(nameSurname.getString(3));
		}
		ResultSet customDataRS = myStatement
				.executeQuery("SELECT CustomData FROM HomeIn.User WHERE UserId = " + Contoller.getUID());

		if (customDataRS.next()) {
			customData.setPromptText(customDataRS.getString(1));
		} else {
			customData.setPromptText("");
		}

		ResultSet customImageRS = myStatement
				.executeQuery("SELECT ProfileImage FROM HomeIn.User WHERE UserId = " + Contoller.getUID());
		System.out.println("SELECT ProfileImage FROM HomeIn.User WHERE UserId = " + Contoller.getUID());
		while (customImageRS.next()) {
			if (customImageRS.getString(1) == null) {
				Image pimage = new Image("http://92.178.96.124/ftpuser/avatar.png");
				customProfileImg.setImage(pimage);
			} else {
				Image pimage = new Image(customImageRS.getString(1));
				customProfileImg.setImage(pimage);

			}
		}

	}

	public void toEdit(ActionEvent event) throws IOException {

		File imgFile = getImage();

		if (!validImage(imgFile)) {
			throw new IOException();
		}
		String extraChars = "";
		Random random = new Random();
		extraChars = Integer.toString(random.nextInt(20000));
		loadImageFromLocalMachine(imgFile);
		storeFileInFtp(imgFile,extraChars);
		updateUserProfileImage(imgFile,extraChars);
	}

	/**
	 * 
	 * @param img
	 * @return
	 */
	private boolean validImage(File img) {
		return img != null;
	}

	/**
	 * 
	 * @param imgFile
	 */
	private void updateUserProfileImage(File imgFile,String extraChars) {
		String ftpFilePath = "http://92.178.96.124/ftpuser/" + imgFile.getName()+extraChars;
		int userId = Contoller.getUID();
		
		try {
			Connection myConnection;
			myConnection = DriverManager.getConnection("jdbc:mysql://92.178.96.124:3306", "judit", "1234");

			PreparedStatement ps = myConnection
					.prepareStatement("UPDATE HomeIn.User SET ProfileImage = ? WHERE UserId = ?");
			ps.setString(1, ftpFilePath);
			ps.setInt(2, userId);
			ps.execute();
		} catch (Exception e) {
			System.out.println("Error in Update query");
		}
	}

	/**
	 * 
	 * @param imgFile
	 */
	private void storeFileInFtp(File imgFile,String extraChars) {
		final String SFTP = "92.178.96.124";
		final String USER = "ftpuser";
		final String PASSWORD = "saurus12";

		// Connect to FTP
		FTPClient client = new FTPClient();
		try {
			client.connect(SFTP);
			client.login(USER, PASSWORD);
			client.setFileType(FTP.BINARY_FILE_TYPE);
			client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
			client.enterLocalPassiveMode();

			FileInputStream fileToUpload = new FileInputStream(imgFile.getAbsolutePath());

			client.storeFile(imgFile.getName()+extraChars, fileToUpload);

		} catch (IOException ioe) {
			System.out.println("Error in FTP");
		}
	}

	/**
	 * 
	 * @param imgFile
	 * @throws FileNotFoundException
	 */
	private void loadImageFromLocalMachine(File imgFile) throws FileNotFoundException {
		// Load image from local machine
		InputStream inputStream = new FileInputStream(imgFile.getAbsolutePath());
		System.out.println(imgFile.getAbsolutePath());
		try {
			Image image = new Image(inputStream);
			customProfileImg.setImage(image);
		} catch (Exception e) {
			System.out.println("Failed to load image");
		}
	}

	/**
	 * 
	 * @return
	 */
	private File getImage() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Tria una imatge de perfil");
		File imgFile = fileChooser.showOpenDialog(null);
		return imgFile;
	}
	
	public void pullBack(ActionEvent event) throws IOException {
		
		root = FXMLLoader.load(getClass().getResource("MainPage.fxml"));

		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
