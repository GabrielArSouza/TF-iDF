package bigdata.techniques;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.executor.ExecutorConstructTerms;
import bigdata.techniques.tools.executor.ExecutorInverseDocument;
import bigdata.techniques.tools.executor.ExecutorReadDocuments;
import bigdata.techniques.tools.executor.ExecutorTFidFTable;
import bigdata.techniques.tools.executor.ExecutorTermFrequency;
import bigdata.techniques.tools.mutex.MutexCounter;

public class ExecutorTFidF extends TFidF{
	
	public ExecutorTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
		
		ArrayList<String> urls = this.readURLs();
		MutexCounter mtxCounter = new MutexCounter(urls.size()-1);
		
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		for (int i=0; i < numberOfThreads; i++) {
			Runnable task = new ExecutorReadDocuments(mtxCounter, urls, documents);
			exec.submit(task);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}

	@Override
	public void constructTerms() {
		
		ArrayList<Document> doc = new ArrayList<Document>();
		doc.addAll(documents.keySet());
	
		// Get Counter
		MutexCounter mtxCounter = new MutexCounter(doc.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);

		for (int i=0; i < numberOfThreads; i++) {
			Runnable task = new ExecutorConstructTerms(mtxCounter, doc, terms);
			exec.submit(task);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void termFrequency() {
		
		// Get document list
		ArrayList<Document> docs = new ArrayList<Document>();
		docs.addAll(documents.keySet());
		
		// Get Counter
		MutexCounter mtxCounter = new MutexCounter(docs.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		for (int i=0; i < numberOfThreads; i++) {
			Runnable task = new ExecutorTermFrequency(mtxCounter, docs, terms, termFrequency);
			exec.submit(task);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void inverseDistance() {

		ArrayList<String> term = new ArrayList<String>();
		term.addAll(this.terms.keySet());

		MutexCounter mtxCounter = new MutexCounter(term.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		for (int i=0; i < numberOfThreads; i++) {
			Runnable task = new ExecutorInverseDocument(mtxCounter, term, documents, inverseDistance);
			exec.submit(task);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void tfidfTable() {
		
		MutexCounter mtxCounter = new MutexCounter(terms.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		for (int i=0; i < numberOfThreads; i++) {
			Runnable task = new ExecutorTFidFTable(mtxCounter, 
					documents, terms, termFrequency, inverseDistance, tfIdf);
			exec.submit(task);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
