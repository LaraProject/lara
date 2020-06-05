package org.lara.nlp.word2vec;

import java.util.ArrayList;
import org.lara.nlp.context.Cornell;
import org.lara.nlp.word2vec.W2v;

class W2vTest {
	public static void main(String[] args) throws Exception {
		// Cornell data
		Cornell context = new Cornell(args[0], args[1], 5, 30);
		System.out.println("W2vTest: movie_lines = " + args[0]);
		System.out.println("W2vTest: movie_conversations = " + args[1]);
		System.out.println("W2vTest: min_length = 5 | max_length = 30");
		System.out.println("W2vTest: initializing...");
		context.init();
		System.out.println("W2vTest: cleaning text...");
		context.cleaning();
		System.out.println("W2vTest: tokenizing...");
		context.tokenize();
		// Word2Vec
		ArrayList<String> allWords = new ArrayList<String>();
		allWords.addAll(context.questions);
		allWords.addAll(context.answers);
		allWords.add("<UNK>");
		allWords.add("<UNK>");
		allWords.add("<UNK>");
		allWords.add("<UNK>");
		allWords.add("<UNK>");
		System.out.println("W2vTest: creating the W2v object...");
		W2v w2v = new W2v(allWords, 5, 5, 5, 100);
		System.out.println("W2vTest: export W2v model to " + args[2]);
		w2v.write_vectors(args[2]);
	}	
}