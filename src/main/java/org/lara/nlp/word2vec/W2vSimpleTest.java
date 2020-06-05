package org.lara.nlp.word2vec;

import java.util.ArrayList;
import org.lara.nlp.context.Simple;
import org.lara.nlp.word2vec.W2v;

class W2vSimpleTest {
	public static void main(String[] args) throws Exception {
		// Simple context data
		int min_length = Integer.parseInt(args[1]);
		int max_length = Integer.parseInt(args[2]);
		Simple context = new Simple(args[0], min_length, max_length);
		System.out.println("W2vSimpleTest: filename = " + args[0]);
		System.out.println("W2vSimpleTest: min_length = " + min_length + " | max_length = " + max_length);
		System.out.println("W2vSimpleTest: initializing...");
		context.init();
		System.out.println("W2vSimpleTest: cleaning text...");
		context.cleaning();
		System.out.println("W2vSimpleTest: tokenizing...");
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
		System.out.println("W2vSimpleTest: creating the W2v object...");
		W2v w2v = new W2v(allWords, 5, 5, 5, 100);
		System.out.println("W2vSimpleTest: export W2v model to " + args[3]);
		w2v.write_vectors(args[3]);
	}	
}
