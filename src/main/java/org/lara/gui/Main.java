package org.lara.gui;
	
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.lara.rnn.Server;


public class Main extends Application {
	
	String Lara; // identité de l'interlocuteur
	String home = "Homepage.fxml"; // la fenêtre d'accueil
	String view = "ChatFrame.fxml"; // la fenêtre de chat
	String stylesheet = "application.css"; // la feuille de style
	String username = "Hacker anonyme"; // à changer avec la fenêtre d'accueil
	// les autres variables utiles sont définies dans les controllers

    // Server RNN
    Server server;

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
		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void closeWindowEvent(WindowEvent event) {
        System.out.println("Window close request ...");

        
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Quitter l'application");
            alert.setContentText(String.format("Vous partez déjà ?"));
            Optional<ButtonType> res = alert.showAndWait();

            System.out.println("here1");
            if(res.isPresent()) {
                if(res.get().equals(ButtonType.CANCEL)) {
                    event.consume();
                } else {
                	// Open a new connection to shutdown the server
                	server = new Server();
	            	if (!(server == null))
	                	server.shutdownServer();
                }
            }
        
    }
	
	
	
	
}
