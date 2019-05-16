package bigdata.techniques;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.mutex.MutexCounter;
import bigdata.techniques.tools.mutex.MutexThreadConstructTerms;
import bigdata.techniques.tools.mutex.MutexThreadInverseDocument;
import bigdata.techniques.tools.mutex.MutexThreadTFidF;
import bigdata.techniques.tools.reactiveStream.ConsumeTermFrequency;
import bigdata.techniques.tools.reactiveStream.ConsumerUrls;
import bigdata.techniques.tools.reactiveStream.ProducerDocuments;
import bigdata.techniques.tools.reactiveStream.ProducerUrls;

public class ReactiveStreamTFidF extends TFidF{

	public ReactiveStreamTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
		ArrayList<String> urls = this.readURLs();
		MutexCounter count = new MutexCounter(urls.size()-1);
		
		SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
		for (int i=0; i < 2; i++){
			ConsumerUrls consumer = new ConsumerUrls(this.documents);
			publisher.subscribe(consumer);
		}
		
		for (int i=0; i < 2; i++){
			ProducerUrls system = new ProducerUrls(publisher, count, urls);
			new Thread(system).start();
		}
		
		do {
			
			try { TimeUnit.MILLISECONDS.sleep(1); }
			catch (InterruptedException e) { e.printStackTrace(); } 
		
		}while ((publisher.estimateMaximumLag() > 0));
		
	}

	@Override// TODO Auto-generated method stub
	public void constructTerms() {
		
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
		
		ArrayList<Document> docs = new ArrayList<>();
		docs.addAll(documents.keySet());
		
		MutexCounter mtx = new MutexCounter(docs.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		
		SubmissionPublisher<Document> publisher = new SubmissionPublisher<>();
		for (int i=0; i < numberOfThreads; i++){
			ConsumeTermFrequency consumer = new ConsumeTermFrequency(terms, termFrequency);
			publisher.subscribe(consumer);
		}
		
		ProducerDocuments system = new ProducerDocuments(publisher, mtx, docs);
		Thread t = new Thread(system);
		t.start();
			
		do {
			
			try { TimeUnit.MILLISECONDS.sleep(1); }
			catch (InterruptedException e) { e.printStackTrace(); } 
		
		}while ((publisher.estimateMaximumLag() > 0));
	}

	@Override
	public void inverseDistance() {
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
