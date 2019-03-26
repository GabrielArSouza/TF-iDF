package bigdata.techniques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.mutex.MutexCounter;
import bigdata.techniques.tools.mutex.MutexInverseDistance;
import bigdata.techniques.tools.mutex.MutexReadDocuments;
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
		MutexCounter count = new MutexCounter(urls.size()-1);
		ArrayList<MutexReadDocuments> threads = new ArrayList<MutexReadDocuments>();
		
		for (int i=0; i<40; i++) {
			threads.add(new MutexReadDocuments(count, urls, this.documents));
			threads.get(i).start();
		}
		
		boolean isComplete = false;
		int alive = 0;
		while (!isComplete) {
			for (MutexReadDocuments t : threads)
				if (t.isAlive()) alive++;
			if (alive == 0) isComplete = true;
			alive = 0;
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
		
		MutexThreadConstructTerms t1 = new MutexThreadConstructTerms(count, doc, this.terms);
		MutexThreadConstructTerms t2 = new MutexThreadConstructTerms(count, doc, this.terms);
		MutexThreadConstructTerms t3 = new MutexThreadConstructTerms(count, doc, this.terms);
		MutexThreadConstructTerms t4 = new MutexThreadConstructTerms(count, doc, this.terms);
		
		t1.start(); t2.start(); t3.start(); t4.start();
		while (t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive())
		{ /* wait all threads terminated */	}
		
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
		
		MutexThreadTermFrequency t1 = new MutexThreadTermFrequency(count, doc, this.terms, this.termFrequency);
		MutexThreadTermFrequency t2 = new MutexThreadTermFrequency(count, doc, this.terms, this.termFrequency);
		MutexThreadTermFrequency t3 = new MutexThreadTermFrequency(count, doc, this.terms, this.termFrequency);
		MutexThreadTermFrequency t4 = new MutexThreadTermFrequency(count, doc, this.terms, this.termFrequency);
		
		t1.start(); t2.start(); t3.start(); t4.start();
		while (t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive())
		{ /* wait all threads terminated */	}
		
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
		
		MutexInverseDistance t1 = new MutexInverseDistance(mtxCounter, term, this.documents, this.inverseDistance);
		MutexInverseDistance t2 = new MutexInverseDistance(mtxCounter, term, this.documents, this.inverseDistance);
		MutexInverseDistance t3 = new MutexInverseDistance(mtxCounter, term, this.documents, this.inverseDistance);
		MutexInverseDistance t4 = new MutexInverseDistance(mtxCounter, term, this.documents, this.inverseDistance);
		
		t1.start(); t2.start(); t3.start(); t4.start();
		while (t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive())
		{ /*wait all threads terminate*/}
		
//		System.out.println("The inverse distance table was built");
	}

	@Override
	public void tfidfTable() {
//		System.out.println("Building the TF-idF table...");
		
		// Get Counter
		MutexCounter count = new MutexCounter(terms.size()-1);
		
		MutexThreadTFidF t1 = new MutexThreadTFidF(count, this.documents,
				this.terms, this.termFrequency, this.inverseDistance, this.tfIdf);
		MutexThreadTFidF t2 = new MutexThreadTFidF(count, this.documents,
				this.terms, this.termFrequency, this.inverseDistance, this.tfIdf);
		MutexThreadTFidF t3 = new MutexThreadTFidF(count, this.documents,
				this.terms, this.termFrequency, this.inverseDistance, this.tfIdf);
		MutexThreadTFidF t4 = new MutexThreadTFidF(count, this.documents,
				this.terms, this.termFrequency, this.inverseDistance, this.tfIdf);
		
		t1.start(); t2.start(); t3.start(); t4.start();
		while (t1.isAlive() || t2.isAlive() || t3.isAlive() || t4.isAlive())
		{ /* wait all threads terminated*/ }
		
//		System.out.println("The TF-idF table was built");		
	}

}
