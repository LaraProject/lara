package org.lara.nlp.word2vec;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.learning.impl.elements.SkipGram;
import org.deeplearning4j.models.embeddings.loader.VectorsConfiguration;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.sequencevectors.SequenceVectors;
import org.deeplearning4j.models.sequencevectors.iterators.AbstractSequenceIterator;
import org.deeplearning4j.models.sequencevectors.serialization.VocabWordFactory;
import org.deeplearning4j.models.sequencevectors.transformers.impl.SentenceTransformer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.wordstore.VocabConstructor;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.AbstractCache;
import org.deeplearning4j.text.sentenceiterator.CollectionSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.factory.Nd4j;

public class Sv {
	// Structure
	SequenceVectors<VocabWord> vectors;

	public Sv(ArrayList<String> sentences) {
		AbstractCache<VocabWord> vocabCache = new AbstractCache.Builder<VocabWord> ().build();
		// First we build line iterator
		SentenceIterator underlyingIterator = new CollectionSentenceIterator(sentences);
		// Convert lines into Sequences of VocabWords with SentenceTransformer
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());
		SentenceTransformer transformer = new SentenceTransformer.Builder()
			.iterator(underlyingIterator)
			.tokenizerFactory(t)
			.build();
		// Pack that transformer into AbstractSequenceIterator
		AbstractSequenceIterator<VocabWord> sequenceIterator =
			new AbstractSequenceIterator.Builder < > (transformer).build();
		// Build vocabulary out of sequence iterator.
		VocabConstructor<VocabWord> constructor = new VocabConstructor.Builder<VocabWord> ()
			.addSource(sequenceIterator, 5)
			.setTargetVocabCache(vocabCache)
			.build();
		constructor.buildJointVocabulary(false, true);
		// Build WeightLookupTable instance for our new model
		WeightLookupTable<VocabWord> lookupTable = new InMemoryLookupTable.Builder<VocabWord> ()
			.vectorLength(150)
			.useAdaGrad(false)
			.cache(vocabCache)
			.build();
		lookupTable.resetWeights(true);
		// Build AbstractVectors model
		vectors = new SequenceVectors.Builder<VocabWord> (new VectorsConfiguration())
			.minWordFrequency(5)
			.lookupTable(lookupTable)
			.iterate(sequenceIterator)
			.vocabCache(vocabCache)
			.batchSize(250)
			.iterations(1)
			.epochs(1)
			.resetModel(false)
			.trainElementsRepresentation(true)
			.trainSequencesRepresentation(false)
			.elementsLearningAlgorithm(new SkipGram<VocabWord> ())
			.build();
		vectors.fit();
	}

	// Load the model
	public Sv(String path) throws Exception {
		vectors = WordVectorSerializer.readSequenceVectors(path, true);
	}

	// Save the model
	public void save_model(String path) throws Exception {
		OutputStream out_file = new FileOutputStream(path);
		WordVectorSerializer.writeSequenceVectors(vectors, out_file);
	}

	// Write vectors
	public void write_vectors(String path) throws Exception {
		save_model(path);
	}

	// Export the model
	public SequenceVectors<VocabWord> getModel() {
		return vectors;
	}
}