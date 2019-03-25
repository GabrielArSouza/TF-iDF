package bigdata.techniques;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.mutex.MutexCounter;
import bigdata.techniques.tools.mutex.MutexThreadConstructTerms;
import bigdata.techniques.tools.mutex.MutexThreadTFidF;
import bigdata.techniques.tools.mutex.MutexThreadTermFrequency;

public class MutexTFidF extends TFidF {

	public MutexTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
		try {
			FileReader file = new FileReader(this.urlDocuments);
			BufferedReader readFile = new BufferedReader(file);
			String line = readFile.readLine();
			int count=1;
			while (line != null) {
				line.trim();
				this.documents.put(new Document(line), count);
				line = readFile.readLine();
				count++;
			}
//			System.out.println( count +  " files were read" );
			file.close();
		} catch (IOException e1) {
			System.err.println("could not open file " + this.urlDocuments 
					+ " " + e1.getMessage());
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
		
		t1.start(); t2.start(); t3.start();
		
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
		
		t1.start(); t2.start(); t3.start();
		
//		System.out.println("The term frequency table was built");
		
	}

	@Override
	public void inverseDistance() {
//		System.out.println("Building the inverse distance table...");
		
		Set<String> termsOfTable = terms.keySet();
		Set<Document> docs = documents.keySet();
		
		double numDocs = (double) this.getNumberOfDocuments();
		int count;
		double value;
		
		for (String term : termsOfTable) {
			count = 0;
	
			for (Document doc : docs) {
				if (doc.numberOfOccurrencesTerm(term)!=0)
					count++;
			}
			value = Math.log(numDocs/(double)count);
			this.inverseDistance.put(term, value);
		}
		
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
		
		t1.start(); t2.start(); t3.start();
		
//		System.out.println("The TF-idF table was built");		
	}

}
