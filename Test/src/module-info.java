module Test {
	requires javafx.controls;
	requires javafx.graphics;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	requires mysql.connector.java;
	requires org.apache.commons.net;
	
	opens application to javafx.graphics, javafx.fxml;
}
