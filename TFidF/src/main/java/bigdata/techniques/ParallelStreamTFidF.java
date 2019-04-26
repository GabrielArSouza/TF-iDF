package bigdata.techniques;

import java.util.ArrayList;
import java.util.Set;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.parallelStream.ParallelStreamConstructTerms;
import bigdata.techniques.tools.parallelStream.ParallelStreamInverseDocument;
import bigdata.techniques.tools.parallelStream.ParallelStreamReadDocuments;
import bigdata.techniques.tools.parallelStream.ParallelStreamTFidFTable;
import bigdata.techniques.tools.parallelStream.ParallelStreamTermFrequency;

public class ParallelStreamTFidF extends TFidF{

	public ParallelStreamTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
		ArrayList<String> urls = this.readURLs();
		ParallelStreamReadDocuments reader = new ParallelStreamReadDocuments(urls, documents);
		reader.run();
	}

	@Override
	public void constructTerms() {
		
		Set<Document> docs = documents.keySet();
		ParallelStreamConstructTerms ct = new 
				ParallelStreamConstructTerms(docs, this.terms);
		ct.run();
	}

	@Override
	public void termFrequency() {
		Set<Document> docs = documents.keySet();
		ParallelStreamTermFrequency tf = new
				ParallelStreamTermFrequency(docs, this.terms, this.termFrequency);
		tf.run();
	}

	@Override
	public void inverseDistance() {

		ParallelStreamInverseDocument id =
			new ParallelStreamInverseDocument
			(	terms.keySet(),
				documents.keySet(),
				this.inverseDistance
			);
		
		id.run();
	}

	@Override
	public void tfidfTable() {

		ParallelStreamTFidFTable tfidf =
			new ParallelStreamTFidFTable
			(documents.keySet(),
			 terms.keySet(), 
			 this.termFrequency,
			 this.inverseDistance,
			 this.tfIdf);
		
		tfidf.run();
		
	}
}
