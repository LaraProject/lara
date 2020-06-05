package org.lara.nlp.context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class Simple extends Context {
	// Structure
	String filename;

	// Constructor
	public Simple(String filename, int min_length, int max_length) {
		this.filename = filename;
		questions = new ArrayList<String> ();
		answers = new ArrayList<String> ();
		this.min_length = min_length;
		this.max_length = max_length;
	}

	// Creating a dictionary that maps each line with its id
	private void get(String filename) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			int count_line = 0;
			boolean skip_next = false;
			while ((line = reader.readLine()) != null) {
				if (count_line % 2 == 0)
					if (line.length() > 11) {
						questions.add(line.substring(11));
					} else
						skip_next = true;
				else
					if (skip_next)
						skip_next = false;
					else
						if (line.length() > 9) {
							answers.add(line.substring(9));
						}
						else
							questions.remove(questions.size()-1);
				count_line = count_line + 1;
			}
			reader.close();
		} catch (Exception e) {
			System.err.format("Exception occurred trying to read '%s'.", filename);
			e.printStackTrace();
		}
	}

	public void init() {
		get(filename);
	}
}