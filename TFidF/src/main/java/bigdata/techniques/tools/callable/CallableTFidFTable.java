package bigdata.techniques.tools.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class CallableTFidFTable implements Callable<Map<String,Double>>{

	private final MutexCounter mtxCounter;
	private final ConcurrentHashMap<Document, Integer> documents;
	private final ConcurrentHashMap<String, Integer> terms;
	private final ConcurrentHashMap<String, Double> termFrequency;
	private final ConcurrentHashMap<String, Double> inverseDistance;
	
	public CallableTFidFTable(MutexCounter mtxCounter, 
			ConcurrentHashMap<Document, Integer> documents,
			ConcurrentHashMap<String, Integer> terms,
			ConcurrentHashMap<String, Double> termFrequency,
			ConcurrentHashMap<String, Double> inverseDistance) 
	{
		super();
		this.mtxCounter = mtxCounter;
		this.documents = documents;
		this.terms = terms;
		this.termFrequency = termFrequency;
		this.inverseDistance = inverseDistance;
	}

	@Override
	public Map<String, Double> call() throws Exception {
		ArrayList<String> term = new ArrayList<String>();
		term.addAll(this.terms.keySet());
		
		Integer pos = null;
		StringBuffer keyValue = new StringBuffer("");
		double value = 0.0;
		double tfValue = 0.0;
		String actualTerm = "";

		Map<String, Double> tfIdf = new HashMap<>();
		
		while((pos = mtxCounter.increment())!=null) {
			
			actualTerm = term.get(pos);
			
			for (Document doc : this.documents.keySet()) {
				
				keyValue.append(actualTerm);
				keyValue.append("+");
				keyValue.append(doc.getName());
				
				if (this.termFrequency.get(keyValue.toString()) != null) {
					tfValue = this.termFrequency.get(keyValue.toString());
					value = tfValue * this.inverseDistance.get(actualTerm);
					tfIdf.put(keyValue.toString(), value);
				}
							
				keyValue.delete(0, keyValue.length());				
			}
		}
		return tfIdf;
	}
	
}
