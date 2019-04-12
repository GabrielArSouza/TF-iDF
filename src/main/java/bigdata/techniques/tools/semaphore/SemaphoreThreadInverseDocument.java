package bigdata.techniques.tools.semaphore;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class SemaphoreThreadInverseDocument extends Thread {

	private final SemaphoreStringController semaphore;
	private final ConcurrentHashMap<Document, Integer> documents;
	private ConcurrentHashMap<String, Double> inverseDocument;
	
	public SemaphoreThreadInverseDocument(
		   SemaphoreStringController semaphore,
		   ConcurrentHashMap<Document, Integer> documents,
		   ConcurrentHashMap<String, Double> inverseDocument) {
		this.semaphore = semaphore;
		this.documents = documents;
		this.inverseDocument = inverseDocument;
	}
	
	public void run () {
		
		Set<Document> docs = documents.keySet();
		
		double numDocs = (double) this.documents.size();
		int count;
		double value;
		String actualTerm= null;
		
		try {
			while ((actualTerm = semaphore.getNext())!=null) {
				count = 0;
				
				for (Document doc : docs) {
					if (doc.numberOfOccurrencesTerm(actualTerm.toString())!=0)
						count++;
				}
				
				value = Math.log(numDocs/(double)count);
				this.inverseDocument.put(actualTerm.toString(), value);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
