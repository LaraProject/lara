package org.lara.nlp.context;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

class Processer {
	// Structure
	public ArrayList<String> questions;
	public ArrayList<String> answers;

	// Limits
	private int min_length;
	private int max_length;

	public Processer(ArrayList<String> questions, ArrayList<String> answers, int min_length, int max_length) {
		this.questions = questions;
		this.answers = answers;
		this.min_length = min_length;
		this.max_length = max_length;
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
		text = text.replaceAll("who's", "who is");
		text = text.replaceAll("here's", "here is");
		text = text.replaceAll("it's", "it is");
		text = text.replaceAll("there's", "there is");
		text = text.replaceAll("\'ll", " will");
		text = text.replaceAll("\'ve", " have");
		text = text.replaceAll("\'re", " are");
		text = text.replaceAll("\'d", " would");
		text = text.replaceAll("n't", " not");
		text = text.replaceAll("won't", "will not");
		text = text.replaceAll("can't", "cannot");
		text = text.replaceAll("<u>","");
		text = text.replaceAll("</u>", "");
		text = text.replaceAll("<i>","");
		text = text.replaceAll("</i>", "");
		text = text.replaceAll("<b>","");
		text = text.replaceAll("</b>", "");
		text = text.replaceAll("[^\\x00-\\x7F]", "");
		text = text.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "");
		text = text.replaceAll("\\p{C}", "");
		text = text.replaceAll("[-()\"#/@;:<>{}`+=~|.!?,\']", "");
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

	// Tokenize
	private String tokenize_sentence(String s) {
		String ret = s;
		int l = (s.split(" ")).length;
		if (l < max_length) {
			for (int i = 0; i < (max_length-l); i++) {
				ret = ret + " <PAD>";
			}
		}
		return "<START> " + ret + " <END>";
	}
	private ArrayList<String> tokenize_set(ArrayList<String> set) {
		ArrayList<String> ret = new ArrayList<String> ();
		for (String s: set) {
			ret.add(tokenize_sentence(s));
		}
		return ret;
	}
	public void tokenize() {
		questions = tokenize_set(questions);
		answers = tokenize_set(answers);
	}

	// Process everything
	public void process() {
		cleanQuestionsAnswers();
		lengthFilter();
	}
}