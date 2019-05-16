package bigdata.techniques.tools.parallelStream;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class ParallelStreamTermFrequency {

	private final Set<Document> documents;
	private final ConcurrentHashMap<String, Integer> terms;
	private ConcurrentHashMap<String, Double> termFrequency;
	
	public ParallelStreamTermFrequency (
		   Set<Document> documents,
		   ConcurrentHashMap<String, Integer> terms,
		   ConcurrentHashMap<String, Double> termFrequency)
	{
		this.documents = documents;
		this.terms = terms;
		this.termFrequency = termFrequency;
	}
	
	public void run ()
	{
		Set<String> termsOfTable = terms.keySet();
		
		documents.parallelStream()
			 .forEach(d -> {
				 // go through all terms
				 termsOfTable.stream()
				 	.forEach(s -> {	
				 		StringBuffer keyValue = new StringBuffer("");
				 		// generate key
				 		keyValue.append(s);
						keyValue.append("+");
						keyValue.append(d.getName());
						
						// get number of times that term appears 
						double numberOfTimesAppears = (double) d.numberOfOccurrencesTerm(s);
						
						// if a valid number insert in table
						if (numberOfTimesAppears != 0.0) {
							this.termFrequency.put(
								keyValue.toString(),
								numberOfTimesAppears/(double) d.getNumberOfTerms());					
						}
						
						// reset key
						keyValue.delete(0, keyValue.length());
				 	});
			 });
	}
}
