package org.lara.nlp.context;

import org.lara.nlp.context.Facebook;

class FacebookTest {
	public static void main(String[] args) throws Exception {
		// Facebook data
		Facebook context = new Facebook(args[0], args[1], 0, 40);
		System.out.println("FacebookTest: json_filename = " + args[0]);
		System.out.println("FacebookTest: answerer = " + args[1]);
		System.out.println("FacebookTest: min_length = 0 | max_length = 40");
		System.out.println("FacebookTest: initializing...");
		context.init();
		System.out.println("FacebookTest: cleaning text...");
		context.cleaning();
		// Export before cleaning
		System.out.println("FacebookTest: exporting to " + args[2]);
		context.exportData(args[2]);
	}	
}
