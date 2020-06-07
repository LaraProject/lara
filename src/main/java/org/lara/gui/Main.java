package org.lara.gui;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;


public class Main extends Application {
	
	String Lara; // identité de l'interlocuteur
	String home = "Homepage.fxml"; // la fenêtre d'accueil
	String view = "ChatFrame.fxml"; // la fenêtre de chat
	String username = "Hacker anonyme"; // à changer avec la fenêtre d'accueil
	// les autres variables utiles sont définies dans les controllers
	
	@Override
	public void start(Stage primaryStage) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getClassLoader().getResource(home));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	
}
