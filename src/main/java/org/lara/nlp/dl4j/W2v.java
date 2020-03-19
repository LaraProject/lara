package org.lara.nlp.dl4j;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import java.util.ArrayList;

public class W2v {
	// Structure
	private Word2Vec vec;

	// Constructor
	public W2v(ArrayList<String> words, Integer minWordFrequency, Integer iterations, Integer epochs,
		Integer dimension) {
		// Iterator
		SentenceIterator iter = new CollectionSentenceIterator(words);
		// Split on white spaces in the line to get words
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		vec = new Word2Vec.Builder()
			.minWordFrequency(minWordFrequency) // exclude words we can't build an accurate context for
			.iterations(iterations)
			.epochs(epochs)
			.layerSize(dimension) // the number of features in the word vector
			.seed(42)
			.windowSize(5) // rolling skip gram window size
			.iterate(iter) // the input sentences
			.tokenizerFactory(t) // the tokenizer
			.build();
		vec.fit();
	}

	// Constructor with a given model
	public W2v(String path) throws Exception {
		vec = WordVectorSerializer.readWord2VecModel(path);
	}

	// Output to a file
	public void write_vectors(String path) throws Exception {
		WordVectorSerializer.writeWordVectors(vec, path);
	}

	// Save the model
	public void save_model(String path) throws Exception {
		vec = WordVectorSerializer.readWord2VecModel(path);
	}

	// Get the cosine similarity
	public double similarity(String word1, String word2) {
		return vec.similarity(word1, word2);
	}

	// Export the model
	public Word2Vec getModel() {
		return vec;
	}
}