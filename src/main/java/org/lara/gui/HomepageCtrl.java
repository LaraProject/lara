package org.lara.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.event.ActionEvent;

public class HomepageCtrl extends Main {

    @FXML
    private TextField usernameChooser;

    @FXML
    private Button usernameButton;

    @FXML
    private Label welcomeLabel0;

    @FXML
    private AnchorPane anchorPane0;

    @FXML
    private Label usernameChoice;

    @FXML
    void chooseKeyPressed(KeyEvent event) throws IOException {
    	Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource(view));
        Scene scene = new Scene(parent);
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        appStage.setScene(scene);
        appStage.show();
    }
    
    @FXML
    void chooseUsername(ActionEvent event) throws IOException {
    	 Parent parent = FXMLLoader.load(getClass().getClassLoader().getResource(view));
         Scene scene = new Scene(parent);
         Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
         //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
         appStage.setScene(scene);
         appStage.show();
    }

}

