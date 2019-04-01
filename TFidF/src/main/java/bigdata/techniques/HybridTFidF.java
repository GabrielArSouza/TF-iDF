package bigdata.techniques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;
import bigdata.techniques.tools.mutex.MutexCounter;
import bigdata.techniques.tools.mutex.MutexThreadInverseDocument;
import bigdata.techniques.tools.mutex.MutexThreadTermFrequency;
import bigdata.techniques.tools.semaphore.SemaphoreStringController;
import bigdata.techniques.tools.semaphore.SemaphoreThreadReadDocuments;
import bigdata.techniques.tools.semaphore.SemaphoreThreadTFidF;

public class HybridTFidF extends TFidF{

	public HybridTFidF(String url) {
		super(url);
		// TODO Auto-generated constructor stub
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
		StopWord sw = StopWordHolder.getStopWord();
		
		Set<Document> docs = documents.keySet();
		for (Document doc : docs) {
			for (String s : doc.getTableTermOccurrence().keySet()) {
				if (!sw.isStopWord(s)) {
					this.terms.put(s, 1);
				}else doc.incrementStopWords(doc.numberOfOccurrencesTerm(s));
			}
		}
		
	}

	@Override
	public void termFrequency() {
		// Get document list
		Set<Document> docs = documents.keySet();
		ArrayList<Document> doc = new ArrayList<Document>();
		for (Document d : docs)
			doc.add(d);
		
		// Get Counter
		MutexCounter count = new MutexCounter(docs.size()-1);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		MutexThreadTermFrequency threads[] = new MutexThreadTermFrequency[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadTermFrequency(count, doc, this.terms, this.termFrequency);
			threads[i].start();
//					try {threads[i].join();} 
//					catch (InterruptedException e) {e.printStackTrace();}			
		}
//				t1.start(); t2.start(); t3.start(); t4.start();
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}

	}

	@Override
	public void inverseDistance() {
		// Get document list
		Set<String> Setterms = this.terms.keySet();
		ArrayList<String> term = new ArrayList<String>();
		for (String t : Setterms)
			term.add(t);

		MutexCounter mtxCounter = new MutexCounter(this.terms.size()-1);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		MutexThreadInverseDocument threads[] = new MutexThreadInverseDocument[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadInverseDocument(mtxCounter, term, this.documents, this.inverseDistance);
			threads[i].start();
		}
		
//				t1.start(); t2.start(); t3.start(); t4.start();
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
		
	}

	@Override
	public void tfidfTable() {
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
