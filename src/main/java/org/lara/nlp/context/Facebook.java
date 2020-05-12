package org.lara.nlp.context;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.ArrayList;

public class Facebook extends Context {
	// Structure
	String answerer;
	boolean prev_is_answer;
	int prev_conv;
	int prev_subconv;
	String cur_sentence;
	JSONArray conversation;

	// Constructor
	public Facebook(String json_filename, String answerer, int min_length, int max_length) {
		this.answerer = answerer;
		questions = new ArrayList<String>();
		answers = new ArrayList<String>();
		this.min_length = min_length;
		this.max_length = max_length;
		this.cur_sentence = "";
		this.prev_conv = -1;
		this.prev_subconv = -1;
		this.prev_is_answer = false;
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader(json_filename)) {
			JSONObject jsonObject = (JSONObject) jsonParser.parse(reader);
			conversation = (JSONArray) jsonObject.get("messages");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
    }

    // Parse a message of a conversation
    private void parseMessage(JSONObject jsonObject) {
    	boolean cur_is_answer = (((String) jsonObject.get("sender_name")).equals(answerer));
    	int cur_conv = ((Long) jsonObject.get("conversationId")).intValue();
    	int cur_subconv = ((Long) jsonObject.get("subConversationId")).intValue();
    	String msg = (String) jsonObject.get("content");
    	if ((!(prev_conv == -1)) && (!(prev_conv == cur_conv))) {
    		if (prev_is_answer) {
    			answers.add(cur_sentence);
    		} else {
    			questions.add(cur_sentence);
    		}
    		cur_sentence = "";	
    		prev_subconv = -1;
    	}
    	if ((prev_subconv == -1) || (prev_subconv == cur_subconv)) {
    		cur_sentence = cur_sentence + " " + msg;
    	} else {
    		if (prev_is_answer) {
    			answers.add(cur_sentence);
    		} else {
    			questions.add(cur_sentence);
    		}
    		cur_sentence = msg;
    	}
    	prev_is_answer = cur_is_answer;
    	prev_conv = cur_conv;
    	prev_subconv = cur_subconv;
    }

    // Parse everything
    public void init() {
    	conversation.forEach(message -> parseMessage((JSONObject) message));
    }
}