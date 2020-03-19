package org.lara.nlp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.ArrayList;

class Cornell extends Context {
	// Structure
	public ArrayList<String> questions;
	public ArrayList<String> answers;
	private HashMap<String,String> id_to_line;
	private ArrayList<String[]> conversations_ids;
	// Limits
	private Integer min_length;
	private Integer max_length;

	// Constructor
	public Cornell(String lines_filename, String conversations_filename, Integer min_length, Integer max_length) {
		id_to_line = getLines(lines_filename);
		conversations_ids = getConversations(conversations_filename);
		questions = new ArrayList<String> ();
		answers = new ArrayList<String> ();
		this.min_length = min_length;
		this.max_length = max_length;
	}

	// Initialize everything
	public void init() {
		getQuestionAnswers();
		cleanQuestionsAnswers();
		lengthFilter();
	}

	// Creating a dictionary that maps each line with its id
	private static HashMap<String,String> getLines(String lines_filename) {
		HashMap<String,String> id_to_line = new HashMap<String,String> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(lines_filename));
			String line;
			String[] _line;
			while ((line = reader.readLine()) != null) {
				_line = line.split(" \\+\\+\\+\\$\\+\\+\\+ ");
				if (_line.length == 5) {
					id_to_line.put(_line[0], _line[4]);
				} else {
					id_to_line.put(_line[0], "");
				}
			}
			reader.close();
			return id_to_line;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", lines_filename);
			e.printStackTrace();
			return null;
		}
	}

	// Creating a list of all of the conversations
	// TODO: Optitmize me (no lists)
	private static ArrayList<String[]> getConversations(String conversations_filename) {
		ArrayList<String[]> conversations_ids = new ArrayList<String[]> ();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(conversations_filename));
			String conversation;
			String _conversation;
			String[] tmp;
			while ((conversation = reader.readLine()) != null) {
				tmp = conversation.split(" \\+\\+\\+\\$\\+\\+\\+ ");
				_conversation = tmp[tmp.length - 1];
				_conversation = _conversation.substring(1, _conversation.length() - 1);
				_conversation = _conversation.replace("'", "");
				_conversation = _conversation.replace(" ", "");
				conversations_ids.add(_conversation.split(","));
			}
			reader.close();
			return conversations_ids;
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", conversations_filename);
			e.printStackTrace();
			return null;
		}
	}

	// Get questions and answers
	private void getQuestionAnswers() {
		for (String[] conversation: conversations_ids) {
			for (int i = 0; i < (conversation.length - 1); i++) {
				questions.add(id_to_line.get(conversation[i]));
				answers.add(id_to_line.get(conversation[i + 1]));
			}
		}
	}

	// Simplifying and cleaning the text using regex
	private static String clean_text(String orig) {
		String text = orig.toLowerCase();
		text = text.replaceAll("i'm", "i am");
		text = text.replaceAll("he's", "he is");
		text = text.replaceAll("she's", "she is");
		text = text.replaceAll("that's", "that is");
		text = text.replaceAll("what's", "what is");
		text = text.replaceAll("where's", "where is");
		text = text.replaceAll("how's", "how is");
		text = text.replaceAll("\'ll", " will");
		text = text.replaceAll("\'ve", " have");
		text = text.replaceAll("\'re", " are");
		text = text.replaceAll("\'d", " would");
		text = text.replaceAll("n't", " not");
		text = text.replaceAll("won't", "will not");
		text = text.replaceAll("can't", "cannot");
		text = text.replaceAll("[-()\"#/@;:<>{}`+=~|.!?,]", "");
		return text;
	}

	// Clean questions and answers
	private void cleanQuestionsAnswers() {
		ArrayList<String> clean_questions = new ArrayList<String> ();
		ArrayList<String> clean_answers = new ArrayList<String> ();
		for (String q: questions)
			clean_questions.add(clean_text(q));
		for (String a: answers)
			clean_answers.add(clean_text(a));
		questions = clean_questions;
		answers = clean_answers;
	}

	// Filter out the questions and answers that are too short or too long
	private void lengthFilter() {
		ArrayList<String> short_questions = new ArrayList<String> ();
		ArrayList<String> short_answers = new ArrayList<String> ();
		int i = 0;
		int l = 0;
		for (String q: questions) {
			l = (q.split(" ")).length;
			if ((min_length <= l) && (l <= max_length)) {
				short_questions.add(q);
				short_answers.add(answers.get(i));
			}
			i++;
		}
		ArrayList<String> clean_questions = new ArrayList<String> ();
		ArrayList<String> clean_answers = new ArrayList<String> ();
		i = 0;
		for (String a: short_answers) {
			l = (a.split(" ")).length;
			if ((min_length <= l) && (l <= max_length)) {
				clean_answers.add(a);
				clean_questions.add(short_questions.get(i));
			}
			i++;
		}
		questions = clean_questions;
		answers = clean_answers;
	}
}