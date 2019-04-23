package bigdata.techniques;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.forkJoin.ForkJoinReader;
import bigdata.techniques.tools.mutex.MutexCounter;
import bigdata.techniques.tools.mutex.MutexThreadConstructTerms;
import bigdata.techniques.tools.mutex.MutexThreadInverseDocument;
import bigdata.techniques.tools.mutex.MutexThreadTFidF;
import bigdata.techniques.tools.mutex.MutexThreadTermFrequency;

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
		
	}

	@Override
	public void inverseDistance() {
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
		
	}

	@Override
	public void tfidfTable() {
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
		
	}

}
