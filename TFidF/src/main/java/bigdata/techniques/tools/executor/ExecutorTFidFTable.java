package bigdata.techniques.tools.executor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class ExecutorTFidFTable implements Runnable {

	private final MutexCounter mtxCounter;
	private final ConcurrentHashMap<Document, Integer> documents;
	private final ConcurrentHashMap<String, Integer> terms;
	private final ConcurrentHashMap<String, Double> termFrequency;
	private final ConcurrentHashMap<String, Double> inverseDistance;
	private ConcurrentHashMap<String, Double> tfIdf;

	
	public ExecutorTFidFTable(MutexCounter mtxCounter, 
			ConcurrentHashMap<Document, Integer> documents,
			ConcurrentHashMap<String, Integer> terms, 
			ConcurrentHashMap<String, Double> termFrequency,
			ConcurrentHashMap<String, Double> inverseDistance, 
			ConcurrentHashMap<String, Double> tfIdf)
	{
		this.mtxCounter = mtxCounter;
		this.documents = documents;
		this.terms = terms;
		this.termFrequency = termFrequency;
		this.inverseDistance = inverseDistance;
		this.tfIdf = tfIdf;
	}

	@Override
	public void run() {
		ArrayList<String> term = new ArrayList<String>();
		term.addAll(this.terms.keySet());
		
		Integer pos = null;
		StringBuffer keyValue = new StringBuffer("");
		double value = 0.0;
		double tfValue = 0.0;
		String actualTerm = "";

		while((pos = mtxCounter.increment())!=null) {
			
			actualTerm = term.get(pos);
			
			for (Document doc : this.documents.keySet()) {
				
				keyValue.append(actualTerm);
				keyValue.append("+");
				keyValue.append(doc.getName());
				
				if (this.termFrequency.get(keyValue.toString()) != null) {
					tfValue = this.termFrequency.get(keyValue.toString());
					value = tfValue * this.inverseDistance.get(actualTerm);
					this.tfIdf.put(keyValue.toString(), value);
				}
							
				keyValue.delete(0, keyValue.length());				
			}
		}
	}

	
}
