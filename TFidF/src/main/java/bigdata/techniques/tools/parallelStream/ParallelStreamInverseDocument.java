package bigdata.techniques.tools.parallelStream;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class ParallelStreamInverseDocument {

	private final Set<String> terms;
	private final Set<Document> documents;
	private ConcurrentHashMap<String, Double> inverseDistance;
	
	public ParallelStreamInverseDocument (
		   Set<String> terms,
		   Set<Document> documents,
		   ConcurrentHashMap<String, Double> inverseDistance)
	{
		this.terms = terms;
		this.documents = documents;
		this.inverseDistance = inverseDistance;
	}
	
	public void run ()
	{
		double numDocs = (double) this.documents.size();
		
		terms.parallelStream()
			 .forEach( s -> {
				 
				 // count number of occurrences of term s
				 long count = 
				 documents.stream()
				  		  .filter( d -> d.numberOfOccurrencesTerm(s) != 0)
				 		  .count();
				 
				 // calculate inverse document
				 double value = Math.log(numDocs/(double)count);
				 this.inverseDistance.put(s, value);
			 });
		
	}
	
}
