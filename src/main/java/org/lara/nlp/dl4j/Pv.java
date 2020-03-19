package org.lara.nlp.dl4j;

import org.deeplearning4j.models.embeddings.learning.impl.sequence.DM;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
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

	// Output to a file
	public void write_vectors(String path) throws Exception {
		WordVectorSerializer.writeParagraphVectors(vectors, path);
	}

	// Convert a sentence to a vector
	public INDArray sentence2vector(String sentence) {
		return vectors.inferVector(sentence);
	}

	// Get the cosine similarity
	public double similarity(String sentence1, String sentence2) {
		return Transforms.cosineSim(sentence2vector(sentence1), sentence2vector(sentence2));
	}

	// Export the model
	public ParagraphVectors getModel() {
		return vectors;
	}
}