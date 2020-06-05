package org.lara.nlp.context;

import org.lara.nlp.context.Simple;

class SimpleTest {
	public static void main(String[] args) throws Exception {
		// Simple data
		int min_length = Integer.parseInt(args[1]);
		int max_length = Integer.parseInt(args[2]);
		Simple context = new Simple(args[0], min_length, max_length);
		System.out.println("SimpleTest: filename = " + args[0]);
		System.out.println("W2vSimpleTest: min_length = " + min_length + " | max_length = " + max_length);
		System.out.println("SimpleTest: initializing...");
		context.init();
		System.out.println("SimpleTest: cleaning text...");
		context.cleaning();
		// Export before cleaning
		System.out.println("SimpleTest: exporting to " + args[3]);
		context.exportData(args[3]);
	}
}
