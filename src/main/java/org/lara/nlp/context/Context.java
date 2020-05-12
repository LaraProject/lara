package org.lara.nlp.context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public abstract class Context {
	public ArrayList<String> questions;
	public ArrayList<String> answers;
	private Processer process;

	public abstract void init();
	// Limits
	public int min_length;
	public int max_length;

	// Filter by length
	public void tokenize() {
		process.tokenize();
		this.questions = process.questions;
		this.answers = process.answers;
	}
	// Execute the cleaning
	public void cleaning() {
		process = new Processer(questions, answers, min_length, max_length);
		process.process();
		this.questions = process.questions;
		this.answers = process.answers;
	}

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

	// Output the data in clean form
	public void exportData(String path) throws Exception {
		FileWriter file_out = new FileWriter(path);
		Iterator<String> it_questions = questions.iterator();
		Iterator<String> it_answers = answers.iterator();
		while (it_questions.hasNext()) {
			file_out.write("Question : " + it_questions.next() + "\n");
			file_out.write("Answer : " + it_answers.next() + "\n");
		}
		file_out.close();
	}
}
