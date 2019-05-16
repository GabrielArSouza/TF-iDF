package bigdata.techniques;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.forkJoin.ForkJoinConstructTerms;
import bigdata.techniques.tools.forkJoin.ForkJoinInverseDocument;
import bigdata.techniques.tools.forkJoin.ForkJoinReader;
import bigdata.techniques.tools.forkJoin.ForkJoinTFidFTable;
import bigdata.techniques.tools.forkJoin.ForkJoinTermFrequency;

public class ForkJoinTFidF extends TFidF{

	public ForkJoinTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
//		System.out.println("Running ForkJoin TFidF");
		ArrayList<String> urls = this.readURLs();
		
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinReader task = new ForkJoinReader(urls, this.documents);
		pool.invoke(task);
		
	}

	@Override
	public void constructTerms() {
		// Get document list
		ArrayList<Document> docs = new ArrayList<>();
		docs.addAll(documents.keySet());
		
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinConstructTerms task = new ForkJoinConstructTerms(docs, terms);
		pool.invoke(task);
		
	}

	@Override
	public void termFrequency() {
		// Get document list
		ArrayList<Document> docs = new ArrayList<>();
		docs.addAll(documents.keySet());
		
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTermFrequency task = new ForkJoinTermFrequency(docs, terms, termFrequency);
		pool.invoke(task);
	
	}

	@Override
	public void inverseDistance() {
		// Get terms list
		ArrayList<String> setTerms = new ArrayList<>();
		setTerms.addAll(this.terms.keySet());
		
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinInverseDocument task = 
				new ForkJoinInverseDocument(setTerms, this.documents.keySet(), this.inverseDistance);
		pool.invoke(task);
	}

	@Override
	public void tfidfTable() {
		// Get terms list
		ArrayList<String> setTerms = new ArrayList<>();
		setTerms.addAll(this.terms.keySet());
		
		ForkJoinPool pool = new ForkJoinPool();
		ForkJoinTFidFTable task =
				new ForkJoinTFidFTable(documents.keySet(), setTerms,
						termFrequency, inverseDistance, tfIdf);
		pool.invoke(task);
	}

}
