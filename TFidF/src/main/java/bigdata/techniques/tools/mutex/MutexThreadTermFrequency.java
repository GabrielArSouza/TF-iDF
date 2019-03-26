package bigdata.techniques.tools.mutex;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class MutexThreadTermFrequency extends Thread{

	private final MutexCounter mtxCounter;
	private final ArrayList<Document> docs;
	private final ConcurrentHashMap<String, Integer> terms;
	private ConcurrentHashMap<String, Double> termFrequency;
	
	public MutexThreadTermFrequency
	(MutexCounter count, ArrayList<Document> docs,
	 ConcurrentHashMap<String, Integer> terms,
	 ConcurrentHashMap<String, Double> termFrequency)
	{
		this.mtxCounter = count;
		this.docs = docs;
		this.terms = terms;
		this.termFrequency = termFrequency;
	}
	
	public void run () {
		
		int pos = 0;
		StringBuffer keyValue = new StringBuffer("");
		Set<String> termsOfTable = terms.keySet();
		double numberOfTimesAppears = 0;
		Document doc;
		System.out.println("Term Frequency: A thread " + this.getId() + " foi iniciada");
		while (!mtxCounter.isLimit()) {
			
			//System.out.println("Counter: " + pos + " - Thread " + this.getId() + " is running...");
			pos = mtxCounter.increment();
			doc = docs.get(pos);
			for (String s : termsOfTable) {
				
				keyValue.append(doc.getName());
				keyValue.append(s);
				numberOfTimesAppears = (double) doc.numberOfOccurrencesTerm(s);
				this.termFrequency.put(keyValue.toString(), numberOfTimesAppears/(double) doc.getNumberOfTerms());
			
				keyValue.delete(0, keyValue.length());
			}
			
		}
		System.out.println("A thread " + this.getId() + " terminou");
	}
	
}
