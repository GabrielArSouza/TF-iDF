package bigdata.techniques.tools.semaphore;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class SemaphoreThreadTermFrequency extends Thread{

	private final SemaphoreDocumentController semaphore;
	private final ConcurrentHashMap<String, Integer> terms;
	private ConcurrentHashMap<String, Double> termFrequency;
	
	public SemaphoreThreadTermFrequency(
		   SemaphoreDocumentController semaphore,
		   ConcurrentHashMap<String, Integer> terms,
		   ConcurrentHashMap<String, Double> termFrequency)
	{
		this.semaphore = semaphore;
		this.terms = terms;
		this.termFrequency = termFrequency;
	}
	
	public void run () {
		
		StringBuffer keyValue = new StringBuffer("");
		Set<String> termsOfTable = terms.keySet();
		double numberOfTimesAppears = 0.0;
		Document doc = null;
		
		try {
			
			while ( (doc = semaphore.getNext()) != null) {

				for (String s : termsOfTable) {
					
					keyValue.append(s);
					keyValue.append("+");
					keyValue.append(doc.getName());
					
					numberOfTimesAppears = (double) doc.numberOfOccurrencesTerm(s);
					if (numberOfTimesAppears != 0.0)
						this.termFrequency.put(keyValue.toString(), numberOfTimesAppears/(double) doc.getNumberOfTerms());
				
					keyValue.delete(0, keyValue.length());
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
