package org.lara.nlp.word2vec;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.glove.Glove;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.factory.Nd4j;

public class Glv {
	// Structure
	Glove glove;

	public Glv(ArrayList<String> sentences) {
		// Creating SentenceIterator wrapping our training corpus
		SentenceIterator iter = new CollectionSentenceIterator(sentences);
		// Split on white spaces in the line to get words
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		Glove glove = new Glove.Builder()
			.iterate(iter)
			.tokenizerFactory(t)
			.alpha(0.75)
			.learningRate(0.1)
			.epochs(25)
			.xMax(100)
			.batchSize(1000)
			.shuffle(true)
			.symmetric(true)
			.build();
		glove.fit();
	}

	// Output to a file
	public void write_vectors(String path) throws Exception {
		WordVectorSerializer.writeWordVectors(glove, path);
	}

	// Export the model
	public Glove getModel() {
		return glove;
	}
}