package org.lara.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
    private AnchorPane chatPane;

    @FXML
    private TextField messageInput;
    
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox chatBox;

    @FXML
    private Button sendButton;
    
    @FXML
    private TextFlow emojiList;
    
    @FXML
    private Button btnEmoji;

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
        	BubbledLabel questionLabel = new BubbledLabel(question);
        	questionLabel.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
            HBox questionHBox =new HBox();
            questionHBox.setMaxWidth(chatBox.getWidth() - 20);
            questionHBox.getChildren().add(questionLabel);
            questionHBox.setAlignment(Pos.BASELINE_RIGHT);
            chatBox.getChildren().add(questionHBox);
            chatBox.setSpacing(10);
        	
        	String answer = "";
		if (server == null)
	        	answer = "Je n'ai pas accès au serveur.";
	        else {
        		answer = server.sendQuestion(Processer.clean_text(question));
        		answer = EmojiUtils.emojify(answer);
        		answer = answer.replaceAll(":eyebrows:", "\\^\\^");
        	}
        	BubbledLabel answerLabel = new BubbledLabel(answer);
        	answerLabel.setBackground(new Background(new BackgroundFill(Color.WHITE,null, null)));
        	answerLabel.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
            HBox answerHBox =new HBox();
            answerHBox.setMaxWidth(chatBox.getWidth() - 20);
            answerHBox.getChildren().add(answerLabel);
            answerHBox.setAlignment(Pos.BASELINE_LEFT);
            chatBox.getChildren().add(answerHBox);
            chatBox.setSpacing(10);            

            messageInput.setText("");
            
        }
        messageInput.requestFocus();
	}
	
	@FXML // from github.com/Oshan96/ChatRoomFX
    void emojiAction(ActionEvent event) {
        if(emojiList.isVisible()){

            emojiList.setVisible(false);
        }else {
            emojiList.setVisible(true);
        }
    }
	
	 public void initialize() {

       for(Node text : emojiList.getChildren()){
           text.setOnMouseClicked(event -> {
               messageInput.setText(messageInput.getText()+" "+((Text)text).getText());
               emojiList.setVisible(false);
           });
       }
	 }
	
}
