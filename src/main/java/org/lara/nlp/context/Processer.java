package org.lara.nlp.context;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import emoji4j.EmojiUtils;

public class Processer {
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
	private static String clean_english(String text) {
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
		return text;
	}
	private static String clean_html(String text) {
		text = text.replaceAll("<u>","");
		text = text.replaceAll("</u>", "");
		text = text.replaceAll("<i>","");
		text = text.replaceAll("</i>", "");
		text = text.replaceAll("<b>","");
		text = text.replaceAll("</b>", "");
		return text;
	}
	private static String clean_emoji(String text) {
		return EmojiUtils.shortCodify(text);
	}
	public static String clean_text(String orig) {
		// Convert to lower case
		String text = orig.toLowerCase();
		// Remove URLs
		text = text.replaceAll("http?://\\S+\\s?", "");
		text = text.replaceAll("https?://\\S+\\s?", "");
		// Clean english
		//text = clean_english(text);
		// Remove HTML code
		text = clean_html(text);
		// Clean emojis
		text = clean_emoji(text);
		// Remove punctuation
		text = text.replaceAll("[\\p{Punct}]&&[^'?!:^-]", "");
		text = text.replaceAll("\\^\\^", ":eyebrows:");
		text = text.replaceAll("([\\p{IsLatin}]*+):++([\\p{IsLatin}]++):++([\\p{IsLatin}]*+)", "$1 #" + "$2" + "# $3");
		text = text.replaceAll("([\\p{IsLatin}]*+)([?!]++)([\\p{IsLatin}]*+)", "$1 $2 $3");
		text = text.replaceAll("([\\p{IsLatin}]*+)[:^-]++([\\p{IsLatin}]*+)", "$1 $2");
		text = text.replaceAll("#",":");
		// Remove line terminators
		text = text.replaceAll("\\r\\n|\\r|\\n", " ");
		// Remove non-letters
		//text = text.replaceAll("[^a-zA-Z ]", "")
		return text;
	}

	// Clean questions and answers
	private void cleanQuestionsAnswers() {
		ArrayList<String> clean_questions = new ArrayList<String> ();
		ArrayList<String> clean_answers = new ArrayList<String> ();
		Iterator<String> it_questions = questions.iterator();
		Iterator<String> it_answers = answers.iterator();
		while (it_questions.hasNext() && it_answers.hasNext()) {
			String question = it_questions.next();
			String answer = it_answers.next();
			String clean_question = clean_text(question);
			String clean_answer = clean_text(answer);
			if ((clean_question.replaceAll(" ","").length() > 0) && (clean_answer.replaceAll(" ","").length() > 0)) {
				clean_questions.add(clean_question);
				clean_answers.add(clean_answer);
			}
		}
		questions = clean_questions;
		answers = clean_answers;
	}

	// Check length of a string
	private boolean checkLength(String str) {
		int l = (str.split(" ")).length;
		return ((min_length <= l) && (l <= max_length));
	}

	// Filter out the questions and answers that are too short or too long
	private void lengthFilter() {
		ArrayList<String> short_questions = new ArrayList<String> ();
		ArrayList<String> short_answers = new ArrayList<String> ();
		Iterator<String> it_questions = questions.iterator();
		Iterator<String> it_answers = answers.iterator();
		while (it_questions.hasNext() && it_answers.hasNext()) {
			String question = it_questions.next();
			String answer = it_answers.next();
			if (checkLength(question) && checkLength(answer)) {
				short_questions.add(question);
				short_answers.add(answer);
			}
		}
		questions = short_questions;
		answers = short_answers;
	}

	// Tokenize
	private String tokenize_sentence(String s) {
		String ret = s;
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
		lengthFilter();
		cleanQuestionsAnswers();
	}
}
