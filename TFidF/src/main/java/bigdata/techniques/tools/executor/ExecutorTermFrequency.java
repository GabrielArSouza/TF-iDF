package bigdata.techniques.tools.executor;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class ExecutorTermFrequency implements Runnable{

	private final MutexCounter mtxCounter;
	private final ArrayList<Document> docs;
	private final ConcurrentHashMap<String, Integer> terms;
	private ConcurrentHashMap<String, Double> termFrequency;
	
	
	public ExecutorTermFrequency(MutexCounter mtxCounter,
			ArrayList<Document> docs,
			ConcurrentHashMap<String, Integer> terms, 
			ConcurrentHashMap<String, Double> termFrequency)
	{
		this.mtxCounter = mtxCounter;
		this.docs = docs;
		this.terms = terms;
		this.termFrequency = termFrequency;
	}

	@Override
	public void run() {
		
		Integer pos = null;
		StringBuffer keyValue = new StringBuffer("");
		Set<String> termsOfTable = terms.keySet();
		double numberOfTimesAppears = 0;
		Document doc;
		
		while ((pos = mtxCounter.increment())!=null) {
			
			doc = docs.get(pos);
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
	}

}
