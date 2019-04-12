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
		
		Integer pos = null;
		StringBuffer keyValue = new StringBuffer("");
		double value = 0.0;
		double tfValue = 0.0;
		String actualTerm = "";
//		
//		System.out.println("TFidF: A thread " + this.getId() + " foi iniciada");
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
//		System.out.println("A thread " + this.getId() + " terminou");
	}
	
}
