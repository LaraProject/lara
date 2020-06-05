package org.lara.nlp.context;

import org.lara.nlp.context.Cornell;

class CornellTest {
	public static void main(String[] args) throws Exception {
		// Cornell data
		Cornell context = new Cornell(args[0], args[1], 5, 30);
		System.out.println("CornellTest: movie_lines = " + args[0]);
		System.out.println("CornellTest: movie_conversations = " + args[1]);
		System.out.println("CornellTest: min_length = 5 | max_length = 30");
		System.out.println("CornellTest: initializing...");
		context.init();
		System.out.println("CornellTest: cleaning text...");
		context.cleaning();
		// Export before cleaning
		System.out.println("CornellTest: exporting to " + args[2]);
		context.exportData(args[2]);
	}
}