package bigdata.techniques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.mutex.MutexCounter;
import bigdata.techniques.tools.mutex.MutexThreadInverseDocument;
import bigdata.techniques.tools.mutex.MutexThreadReadDocuments;
import bigdata.techniques.tools.mutex.MutexThreadConstructTerms;
import bigdata.techniques.tools.mutex.MutexThreadTFidF;
import bigdata.techniques.tools.mutex.MutexThreadTermFrequency;

public class MutexTFidF extends TFidF {

	public MutexTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
		
		ArrayList<String> urls = new ArrayList<String>();
		
		try {
			
			FileReader file = new FileReader(this.urlDocuments);
			BufferedReader readFile = new BufferedReader(file);
			String line = readFile.readLine();

			while (line != null) {
				line.trim();
				urls.add(line);
				line = readFile.readLine();
			}
			file.close();
		} catch (IOException e1) {
			System.err.println("could not open file " + this.urlDocuments 
					+ " " + e1.getMessage());
		}
		
		// Get Counter
	    int numberOfThreads = 10;
		MutexCounter count = new MutexCounter(urls.size()-1);
		MutexThreadReadDocuments threads[] = new MutexThreadReadDocuments[numberOfThreads];
		
		
		for (int i=0; i<numberOfThreads; i++) {
			threads[i] = new MutexThreadReadDocuments(count, urls, this.documents);
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
//		System.out.println("Building the terms table...");
		
		// Get document list
		Set<Document> docs = documents.keySet();
		ArrayList<Document> doc = new ArrayList<Document>();
		for (Document d : docs)
			doc.add(d);
		
		// Get Counter
		MutexCounter count = new MutexCounter(docs.size()-1);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		MutexThreadConstructTerms threads[] = new MutexThreadConstructTerms[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadConstructTerms(count, doc, this.terms);
			threads[i].start();
//			try {threads[i].join();} 
//			catch (InterruptedException e) {e.printStackTrace();}			
		}
		
//		t1.start(); t2.start(); t3.start(); t4.start();
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
		
//		System.out.println("Terms table built");
	}

	@Override
	public void termFrequency() {
//		System.out.println("Building the term frequency table...");
		
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
//			try {threads[i].join();} 
//			catch (InterruptedException e) {e.printStackTrace();}			
		}
//		t1.start(); t2.start(); t3.start(); t4.start();
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
//		System.out.println("The term frequency table was built");
		
	}

	@Override
	public void inverseDistance() {
//		System.out.println("Building the inverse distance table...");
		
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
		
//		t1.start(); t2.start(); t3.start(); t4.start();
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
		
//		System.out.println("The inverse distance table was built");
	}

	@Override
	public void tfidfTable() {
//		System.out.println("Building the TF-idF table...");
		
		// Get Counter
		MutexCounter count = new MutexCounter(terms.size()-1);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		MutexThreadTFidF threads[] = new MutexThreadTFidF[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadTFidF(count, this.documents,
					this.terms, this.termFrequency, this.inverseDistance, this.tfIdf);
			threads[i].start();
			try {threads[i].join();} 
			catch (InterruptedException e) {e.printStackTrace();}			
		}
		
//		t1.start(); t2.start(); t3.start(); t4.start();
		boolean isAlive = true;
		while ( isAlive ){
			isAlive = false;
			for (int i=0; i < numberOfThreads; i++)
				if (threads[i].isAlive()) isAlive = true;
		}
		
//		System.out.println("The TF-idF table was built");		
	}

}
