package org.lara.nlp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

abstract class Context {
	public ArrayList<String> questions;
	public ArrayList<String> answers;
	public abstract void init();

	// Save the questions and answers
	public void save(String path_questions, String path_answers) throws Exception {
		FileWriter writer_questions = new FileWriter(path_questions);
		FileWriter writer_answers = new FileWriter(path_answers);
		for (String str: questions) {
			writer_questions.write(str + System.lineSeparator());
		}
		for (String str: answers) {
			writer_answers.write(str + System.lineSeparator());
		}
		writer_questions.close();
		writer_answers.close();
	}

	// Restore from a file
	public void restore(String path_questions, String path_answers) {
		questions = new ArrayList<String> ();
		answers = new ArrayList<String> ();
		try {
			BufferedReader reader_questions = new BufferedReader(new FileReader(path_questions));
			String line;
			while ((line = reader_questions.readLine()) != null) {
				questions.add(line);
			}
			reader_questions.close();
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", path_questions);
			e.printStackTrace();
		}
		try {
			BufferedReader reader_answers = new BufferedReader(new FileReader(path_answers));
			String line;
			while ((line = reader_answers.readLine()) != null) {
				questions.add(line);
			}
			reader_answers.close();
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", path_answers);
			e.printStackTrace();
		}
	}
}