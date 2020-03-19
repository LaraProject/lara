package org.lara.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
	
	JFrame homepage = new JFrame("LaraProject");
	JFrame choosePlayerFrame = new JFrame("LaraProject");
	JFrame chatFrame = new JFrame("LaraProject");
	
	JButton sendMessage;
	JButton talkToLouis; // choisir à qui parler
	JButton talkToAnna;
	JTextField  usernameChooser; // choisir son nom (sans conséquences)
	String username;
	
    JTextField  messageBox;
    JTextArea   chatBox;
    
    public static final Color VERY_LIGHT_YELLOW = new Color(255,255,204);
    
    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(new Runnable() { // pour maj le gui
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Main mainGUI = new Main();
                mainGUI.enter();
            }
        });
    }
    
    public void enter() {
    	
    	chatFrame.setVisible(false);
    	choosePlayerFrame.setVisible(false);
    	
    	JLabel welcome = new JLabel("Bienvenue sur le chat !");
    	JButton continuer = new JButton("Continuer...");
    	continuer.addActionListener(new VersChoix());

    	welcome.setFont(new Font("Courier New", Font.PLAIN, 15));
    	continuer.setFont(new Font("Courier New", Font.PLAIN, 15));
        homepage.add(BorderLayout.CENTER, welcome);
        homepage.add(BorderLayout.SOUTH, continuer);
        homepage.setSize(470, 300);
        homepage.getContentPane().setBackground(VERY_LIGHT_YELLOW);
        homepage.setLocationRelativeTo(null);
        homepage.setVisible(true);
    	
    }
    
    public void chooseSomeone() {

        
    	choosePlayerFrame.setSize(470, 300);
    	
    	JLabel descr = new JLabel("Choisir un pseudo et une personne à qui parler");
    	
        usernameChooser = new JTextField(15);
        JLabel chooseUsernameLabel = new JLabel("Pseudo :");
        
        talkToLouis = new JButton("Parler avec Louis");
        talkToAnna = new JButton("Parler avec Anna");
        talkToLouis.addActionListener(new enterServerLouis()); // coder Louis
        //talkToAnna.addActionListener(new enterServerAnna()); // coder Anna
        
        JPanel pan = new JPanel(new GridBagLayout());
        pan.setPreferredSize(new Dimension(300, 120));
        pan.setBackground(Color.WHITE);
        
        GridBagConstraints preRight = new GridBagConstraints();
        //preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;
        
        // Création des boutons de manière très très rudimentaire
        JPanel buttonAnna = new JPanel();
        buttonAnna.add(talkToAnna);
        //cell1.setBackground(Color.YELLOW);
        //cell1.setPreferredSize(new Dimension(100, 40));		
        JPanel buttonLouis = new JPanel();
        buttonLouis.add(talkToLouis);
        //cell2.setBackground(Color.red);
        //cell2.setPreferredSize(new Dimension(100, 40));
        JPanel userLabel = new JPanel();
        userLabel.add(chooseUsernameLabel);
        //cell3.setBackground(Color.green);
        //cell3.setPreferredSize(new Dimension(100, 40));
        JPanel inputZone = new JPanel();
        inputZone.add(usernameChooser, preRight);
        //cell4.setBackground(Color.black);
        //cell4.setPreferredSize(new Dimension(200, 200));
        JPanel description = new JPanel();
        description.add(descr);
        //Le conteneur principal
        //JPanel content = new JPanel();
        //content.setPreferredSize(new Dimension(300, 120));
        //content.setBackground(Color.WHITE);
        //On définit le layout manager
        //content.setLayout(new GridBagLayout());
    		
        //L'objet servant à positionner les composants
        GridBagConstraints gbc = new GridBagConstraints();
    		
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        pan.add(description, gbc);
        
        //---------------------------------------------
        gbc.gridx = 0;
        gbc.gridy = 1;
        pan.add(userLabel, gbc);
        //---------------------------------------------
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 2;
        gbc.gridy = 1;
        pan.add(inputZone);
        //---------------------------------------------
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 1;
        gbc.gridy = 2;
        pan.add(buttonAnna, gbc);
        //---------------------------------------------
        gbc.gridwidth = GridBagConstraints.REMAINDER; //Cette instruction informe le layout que c'est une fin de ligne
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridheight = 1;
        pan.add(buttonLouis, gbc);
        //---------------------------------------------
        		
        //---------------------------------------------
        /*gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        */
        //---------------------------------------------
        
        choosePlayerFrame.setContentPane(pan);
        choosePlayerFrame.getContentPane().setBackground(VERY_LIGHT_YELLOW);
        choosePlayerFrame.setLocationRelativeTo(null);
        choosePlayerFrame.setVisible(true);

    }
    
    class VersChoix implements ActionListener {
    	
    	public void actionPerformed(ActionEvent event) {
    		homepage.setVisible(false);
    		chooseSomeone();
    	}
    }
    
    public void displayLouis() { // a personnaliser avec les word2vec etc.
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);

        chatFrame.add(mainPanel);
        chatFrame.setSize(470, 300);
        chatFrame.setLocationRelativeTo(null);
        chatFrame.getContentPane().setBackground(VERY_LIGHT_YELLOW);
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setVisible(true);

    }
    
    public void displayAnna() { // a personnaliser avec les word2vec etc.
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sendMessage = new JButton("Send Message");
        sendMessage.addActionListener(new sendMessageButtonListener());

        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);

        mainPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.LINE_START;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        mainPanel.add(BorderLayout.SOUTH, southPanel);
/*
        newFrame.add(mainPanel);
        newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newFrame.setSize(470, 300);
        newFrame.setVisible(true);
*/
    }
    
    
    class enterServerLouis implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            } else {
                choosePlayerFrame.setVisible(false);
                displayLouis();
            }
        }
    }
    
    class enterServerAnna implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            } else {
                choosePlayerFrame.setVisible(false);
                displayAnna();
            }
        }
    }
    
    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText() + "\n");
                
                
             /* fonction qui envoie messageBox.getText() au NLP
              * chatBox.append("<Lara>:  " + reponse(messageBox.getText()) + "\n");
              */
                messageBox.setText("");
                
            }
            messageBox.requestFocusInWindow();
        }
    }
    
    // Fonction qui permet la conversation entre user et Lara
    // word2vec et vec2word
    public String reponse(String message) { return null;}
    

}