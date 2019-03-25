package bigdata.techniques.tools.mutex;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class MutexThreadTFidF extends Thread {

	private final MutexCounter mtxCounter;
	private final ConcurrentHashMap<Document, Integer> documents;
	private final ConcurrentHashMap<String, Integer> terms;
	private final ConcurrentHashMap<String, Double> termFrequency;
	private final ConcurrentHashMap<String, Double> inverseDistance;
	private ConcurrentHashMap<String, Double> tfIdf;
	
	public MutexThreadTFidF 
	( MutexCounter mtxCounter,
	  ConcurrentHashMap<Document, Integer> documents,
	  ConcurrentHashMap<String, Integer> terms,
	  ConcurrentHashMap<String, Double> termFrequency,
	  ConcurrentHashMap<String, Double> inverseDistance,
	  ConcurrentHashMap<String, Double> tfIdf
	 )
	{
		this.mtxCounter = mtxCounter;
		this.documents = documents;
		this.terms = terms;
		this.termFrequency = termFrequency;
		this.inverseDistance = inverseDistance;
		this.tfIdf = tfIdf;
	}
	
	public void run() {
		
		ArrayList<String> term = new ArrayList<String>();
		for (String s : this.terms.keySet())
			term.add(s);
		
		int pos = 0;
		StringBuffer keyValue = new StringBuffer("");
		double value = 0.0;
		double tfValue = 0.0;
		String actualTerm = "";
		
		while(!mtxCounter.isLimit()) {
			actualTerm = term.get(pos);
			for (Document doc : this.documents.keySet()) {
				keyValue.append(doc.getName());
				keyValue.append(actualTerm);
				
				if (this.termFrequency.get(keyValue.toString()) == null)
					tfValue = 0.0;
				else tfValue = this.termFrequency.get(keyValue.toString());
				value = tfValue * this.inverseDistance.get(actualTerm);
				this.tfIdf.put(keyValue.toString(), value);
				
				keyValue.delete(0, keyValue.length());				
			}
		}		
	}
	
}
