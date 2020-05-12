package org.lara.nlp.word2vec;

import java.io.File;
import java.io.FileWriter;
import org.deeplearning4j.models.embeddings.learning.impl.sequence.DM;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;

public class Pv {
	// Structure
	ParagraphVectors vectors;

	// Constructor
	public Pv(WordVectors model) {
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		vectors = new ParagraphVectors.Builder()
			.useExistingWordVectors(model)
			.tokenizerFactory(t)
			.build();
	}

	// Load the model
	public Pv(String path) throws Exception {
		vectors = WordVectorSerializer.readParagraphVectors(path);
	}

	// Save model
	public void save_model(String path) throws Exception {
		write_vectors(path);
	}

	// Output to a file
	public void write_vectors(String path) throws Exception {
		WordVectorSerializer.writeParagraphVectors(vectors, path);
	}

	// Export the model
	public ParagraphVectors getModel() {
		return vectors;
	}
}