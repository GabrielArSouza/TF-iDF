package bigdata.techniques.tools.semaphore;

import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class SemaphoreThreadTFidF extends Thread {

	private final SemaphoreStringController semaphore;
	private final ConcurrentHashMap<Document, Integer> documents;
	private final ConcurrentHashMap<String, Double> termFrequency;
	private final ConcurrentHashMap<String, Double> inverseDistance;
	private ConcurrentHashMap<String, Double> tfIdf;
	
	public SemaphoreThreadTFidF(
		   SemaphoreStringController semaphore,
		   ConcurrentHashMap<Document, Integer> documents,
		   ConcurrentHashMap<String, Double> termFrequency,
		   ConcurrentHashMap<String, Double> inverseDocument,
		   ConcurrentHashMap<String, Double> tfIdf) {
		
		this.semaphore = semaphore;
		this.documents = documents;
		this.termFrequency = termFrequency;
		this.inverseDistance = inverseDocument;
		this.tfIdf = tfIdf;
		
	}
	
	public void run () {
		
		StringBuffer keyValue = new StringBuffer("");
		double value = 0.0;
		double tfValue = 0.0;
		String actualTerm = null;
	
		try {
			while((actualTerm = semaphore.getNext())!= null) {
				
				for (Document doc : this.documents.keySet()) {
					
					keyValue.append(actualTerm.toString());
					keyValue.append("+");
					keyValue.append(doc.getName());
					
					if (this.termFrequency.get(keyValue.toString()) != null) {
						tfValue = this.termFrequency.get(keyValue.toString());
						value = tfValue * this.inverseDistance.get(actualTerm.toString());
						this.tfIdf.put(keyValue.toString(), value);
					}
								
					keyValue.delete(0, keyValue.length());				
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
}
