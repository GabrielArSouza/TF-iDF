package bigdata.techniques.tools.executor;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class ExecutorInverseDocument implements Runnable {

	private final MutexCounter mtxCounter;
	private final ArrayList<String> terms;
	private final ConcurrentHashMap<Document, Integer> documents;
	private ConcurrentHashMap<String, Double> inverseDistance;

	
	public ExecutorInverseDocument(MutexCounter mtxCounter,
			ArrayList<String> terms,
			ConcurrentHashMap<Document, Integer> documents,
			ConcurrentHashMap<String, Double> inverseDistance) 
	{
		this.mtxCounter = mtxCounter;
		this.terms = terms;
		this.documents = documents;
		this.inverseDistance = inverseDistance;
	}

	@Override
	public void run() {
		Set<Document> docs = documents.keySet();
		
		double numDocs = (double) this.documents.size();
		int count;
		Integer pos = null;
		double value;
		String actualTerm;
		
		while ((pos = mtxCounter.increment())!= null) {
			
			actualTerm = this.terms.get(pos);
			count = 0;
			
			for (Document doc : docs) {
				if (doc.numberOfOccurrencesTerm(actualTerm)!=0)
					count++;
			}
			
			value = Math.log(numDocs/(double)count);
			this.inverseDistance.put(actualTerm, value);
		}	
	}

}
