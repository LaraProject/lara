package org.lara.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
//import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.lara.rnn.Server;
import org.lara.nlp.context.Processer;
import emoji4j.EmojiUtils;

public class ChatFrameCtrl extends Main {
	
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button buttonRiad;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Button buttonAnna;

    @FXML
    private Button buttonLouis;

    @FXML
    private Button buttonAntoine;
    
    @FXML
    private Button exitButton;

    @FXML
    private TextField messageInput;

    @FXML
    private VBox chatBox;

    @FXML
    private Button sendButton;

    // Server RNN
    Server server;

    @FXML
    void idLouis(ActionEvent event) {
    	Lara = "Louis";
    	idLara(1);
    }

    @FXML
    void idAnna(ActionEvent event) {
    	Lara = "Anna";
    	idLara(2);
    }

    @FXML
    void idRiad(ActionEvent event) {
    	Lara = "Riad";
    	idLara(3);
    }

    @FXML
    void idAntoine(ActionEvent event) {
    	Lara = "Antoine";
    	idLara(4);
    }
    
	@FXML
    void exitLara(ActionEvent event) {
    	Stage stage = (Stage) exitButton.getScene().getWindow();
        if (!(server == null))
        	server.shutdownServer();
        stage.close();
    }

    @FXML
    void sendMessage(ActionEvent event) {
    	send();
    }
	
	@FXML
	private void sendKeyPressed(KeyEvent keyEvent) {
	    if (keyEvent.getCode() == KeyCode.ENTER) {
	    	send();
	    }
	}
	
	private void idLara(int id) {
    	if (server == null)
    		server = new Server();
        if (!(server == null))
        	server.switchPersonn(id);
    	chatBox.getChildren().clear();
	}
	
	private void send() {
		if (messageInput.getText().length() < 1) {
            // do nothing
        } else if (messageInput.getText().equals(".clear")) {
        	chatBox.getChildren().clear();
        } else {
        	String question = messageInput.getText();
        	Label questionLabel = new Label(question);
            HBox questionHBox =new HBox();
            questionHBox.getChildren().add(questionLabel);
            questionHBox.setAlignment(Pos.BASELINE_RIGHT);
            chatBox.getChildren().add(questionHBox);
            chatBox.setSpacing(10);
        	
        	String answer = server.sendQuestion(Processer.clean_text(question));
        	answer = EmojiUtils.emojify(answer);
        	answer = answer.replaceAll(":eyebrows:", "\\^\\^");
        	Label answerLabel = new Label(answer);
            HBox answerHBox =new HBox();
            answerHBox.getChildren().add(answerLabel);
            answerHBox.setAlignment(Pos.BASELINE_LEFT);
            chatBox.getChildren().add(answerHBox);
            chatBox.setSpacing(10);

            messageInput.setText("");
            
        }
        messageInput.requestFocus();
	}

	
}
