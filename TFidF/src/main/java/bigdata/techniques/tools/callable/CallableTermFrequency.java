package bigdata.techniques.tools.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class CallableTermFrequency implements Callable<Map<String,Double>> {

	private final MutexCounter mtxCounter;
	private final ArrayList<Document> docs;
	private final ConcurrentHashMap<String, Integer> terms;
	
	public CallableTermFrequency(MutexCounter mtxCounter,
			ArrayList<Document> docs,
			ConcurrentHashMap<String, Integer> terms) 
	{
		super();
		this.mtxCounter = mtxCounter;
		this.docs = docs;
		this.terms = terms;
	}

	@Override
	public Map<String, Double> call() throws Exception {
		
		Integer pos = null;
		StringBuffer keyValue = new StringBuffer("");
		Set<String> termsOfTable = terms.keySet();
		double numberOfTimesAppears = 0;
		Document doc;
		Map<String, Double> termFrequency = new HashMap<>();
		
		while ((pos = mtxCounter.increment())!=null) {
			
			doc = docs.get(pos);
			for (String s : termsOfTable) {
				
				keyValue.append(s);
				keyValue.append("+");
				keyValue.append(doc.getName());
				
				numberOfTimesAppears = (double) doc.numberOfOccurrencesTerm(s);
				if (numberOfTimesAppears != 0.0)
					termFrequency.put(keyValue.toString(), numberOfTimesAppears/(double) doc.getNumberOfTerms());
			
				keyValue.delete(0, keyValue.length());
			}
		}

		return termFrequency;
	}
	
}
