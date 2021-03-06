package bigdata.techniques;

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
		ArrayList<String> urls = this.readURLs();
		
		// Get Counter
	    int numberOfThreads = 7;
		MutexCounter count = new MutexCounter(urls.size()-1);
		MutexThreadReadDocuments threads[] = new MutexThreadReadDocuments[numberOfThreads];
		
		
		for (int i=0; i<numberOfThreads; i++) {
			threads[i] = new MutexThreadReadDocuments(count, urls, this.documents);
			threads[i].start();
		}	
		
		for (int i=0; i<numberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//System.out.println(documents.keySet().size() + " documentos lidos");
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
		int numberOfThreads = this.getNumberOfCores();
		MutexThreadConstructTerms threads[] = new MutexThreadConstructTerms[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadConstructTerms(count, doc, this.terms);
			threads[i].start();		
		}
		
		for (int i=0; i<numberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		int numberOfThreads = this.getNumberOfCores();
		MutexThreadTermFrequency threads[] = new MutexThreadTermFrequency[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadTermFrequency(count, doc, this.terms, this.termFrequency);
			threads[i].start();
		
		}

		for (int i=0; i<numberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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
		int numberOfThreads = this.getNumberOfCores();
		MutexThreadInverseDocument threads[] = new MutexThreadInverseDocument[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadInverseDocument(mtxCounter, term, this.documents, this.inverseDistance);
			threads[i].start();
		}
		
		for (int i=0; i<numberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
//		System.out.println("The inverse distance table was built");
	}

	@Override
	public void tfidfTable() {
//		System.out.println("Building the TF-idF table...");
		
		// Get Counter
		MutexCounter count = new MutexCounter(terms.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		MutexThreadTFidF threads[] = new MutexThreadTFidF[numberOfThreads];
		
		for (int i=0; i < numberOfThreads; i++) {
			threads[i] = new MutexThreadTFidF(count, this.documents,
					this.terms, this.termFrequency, this.inverseDistance, this.tfIdf);
			threads[i].start();		
		}
		
		for (int i=0; i<numberOfThreads; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
//		System.out.println("The TF-idF table was built");		
	}

}
