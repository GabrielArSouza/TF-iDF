package bigdata.techniques.tools.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;


public class CallableInverseDistance implements Callable<Map<String, Double>>{

	private final MutexCounter mtxCounter;
	private final ArrayList<String> terms;
	private final ConcurrentHashMap<Document, Integer> documents;
	
	public CallableInverseDistance(MutexCounter mtxCounter,
			ArrayList<String> terms,
			ConcurrentHashMap<Document, Integer> documents) 
	{
		super();
		this.mtxCounter = mtxCounter;
		this.terms = terms;
		this.documents = documents;
	}
	
	@Override
	public Map<String, Double> call() throws Exception {
	
		Set<Document> docs = documents.keySet();
		
		double numDocs = (double) this.documents.size();
		int count;
		Integer pos = null;
		double value;
		String actualTerm;
		
		Map<String, Double> inverseDistance = new HashMap<>();
		
		while ((pos = mtxCounter.increment())!= null) {
			
			actualTerm = this.terms.get(pos);
			count = 0;
			
			for (Document doc : docs) {
				if (doc.numberOfOccurrencesTerm(actualTerm)!=0)
					count++;
			}
			
			value = Math.log(numDocs/(double)count);
			inverseDistance.put(actualTerm, value);
		}
		
		return inverseDistance;
	}

}
