package bigdata.techniques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.mutex.MutexCounter;
import bigdata.techniques.tools.mutex.MutexThreadReadDocuments;
import bigdata.techniques.tools.mutex.MutexThreadTermFrequency;
import bigdata.techniques.tools.mutex.MutexThreadConstructTerms;
import bigdata.techniques.tools.mutex.MutexThreadInverseDocument;
import bigdata.techniques.tools.semaphore.SemaphoreDocumentController;
import bigdata.techniques.tools.semaphore.SemaphoreThreadReadDocuments;
import bigdata.techniques.tools.semaphore.SemaphoreThreadTFidF;
import bigdata.techniques.tools.semaphore.SemaphoreThreadTermFrequency;
import bigdata.techniques.tools.semaphore.SemaphoreStringController;
import bigdata.techniques.tools.semaphore.SemaphoreThreadConstructTerms;
import bigdata.techniques.tools.semaphore.SemaphoreThreadInverseDocument;

public class SemaphoreTFidF extends TFidF {

	public SemaphoreTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
		Stack<String> urls = new Stack<String>();
		
		try {
			
			FileReader file = new FileReader(this.urlDocuments);
			BufferedReader readFile = new BufferedReader(file);
			String line = readFile.readLine();

			while (line != null) {
				line.trim();
				urls.push(line);
				line = readFile.readLine();
			}
			file.close();
		} catch (IOException e1) {
			System.err.println("could not open file " + this.urlDocuments 
					+ " " + e1.getMessage());
		} 
		
		// Get Counter
	    int numberOfThreads = 10;
		SemaphoreStringController semaphore = new SemaphoreStringController(urls);
		SemaphoreThreadReadDocuments threads[] = new SemaphoreThreadReadDocuments[numberOfThreads];
		
		
		for (int i=0; i<numberOfThreads; i++) {
			threads[i] = new SemaphoreThreadReadDocuments(documents, semaphore);
			threads[i].start();
		}	
		
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
		
	}

	@Override
	public void constructTerms() {
		
		// Get document list
		Set<Document> documents = this.documents.keySet();
		Stack<Document> docs = new Stack<Document>();
		for (Document d : documents)
			docs.push(d);
		
		// Get Counter
		SemaphoreDocumentController semaphore = new SemaphoreDocumentController(docs);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		SemaphoreThreadConstructTerms threads[] = new SemaphoreThreadConstructTerms[numberOfThreads];

		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new SemaphoreThreadConstructTerms(this.terms, semaphore);
			threads[i].start();		
		}
		
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
	}

	@Override
	public void termFrequency() {
		// Get document list
		Set<Document> documents = this.documents.keySet();
		Stack<Document> docs = new Stack<Document>();
		for (Document d : documents)
			docs.push(d);
	
		
		SemaphoreDocumentController semaphore = new SemaphoreDocumentController(docs);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		SemaphoreThreadTermFrequency threads[] = new SemaphoreThreadTermFrequency[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new SemaphoreThreadTermFrequency(semaphore, this.terms, this.termFrequency);
			threads[i].start();
		}
		
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
	}

	@Override
	public void inverseDistance() {
		// Get Term list
		Set<String> Setterms = this.terms.keySet();
		Stack<String> term = new Stack<String>();
		for (String t : Setterms)
			term.push(t);	
			
		SemaphoreStringController semaphore = new SemaphoreStringController(term);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		SemaphoreThreadInverseDocument threads[] = new SemaphoreThreadInverseDocument[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new SemaphoreThreadInverseDocument(semaphore, this.documents, this.inverseDistance);
			threads[i].start();
		}
		
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
	
	}

	@Override
	public void tfidfTable() {
		// Get Term list
		Set<String> Setterms = this.terms.keySet();
		Stack<String> term = new Stack<String>();
		for (String t : Setterms)
			term.push(t);	
		
		SemaphoreStringController semaphore = new SemaphoreStringController(term);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		SemaphoreThreadTFidF threads[] = new SemaphoreThreadTFidF[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new SemaphoreThreadTFidF(semaphore, documents, 
					termFrequency, inverseDistance, tfIdf);
			threads[i].start();
		}
		
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
		
	}

}
