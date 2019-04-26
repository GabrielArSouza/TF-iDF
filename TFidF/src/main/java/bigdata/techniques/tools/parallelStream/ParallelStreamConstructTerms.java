package bigdata.techniques.tools.parallelStream;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;

public class ParallelStreamConstructTerms {

	private final Set<Document> documents;
	private ConcurrentHashMap<String, Integer> terms;
	
	public ParallelStreamConstructTerms 
	( Set<Document> documents,
	  ConcurrentHashMap<String, Integer> terms)
	{
		this.documents = documents;
		this.terms = terms;
	}
	
	public void run ()
	{
		StopWord sw = StopWordHolder.getStopWord();

		// percorrer a lista de documentos
		documents.parallelStream()
		         .forEach( d -> {
		        	 Set<String> terms = d.getTableTermOccurrence().keySet();
		 			 terms.stream()
		 			      .forEach( s -> {
		 			    	  if (!sw.isStopWord(s))
		 						 this.terms.put(s, 1);
		 					  else d.incrementStopWords(d.numberOfOccurrencesTerm(s));
		 				  });
		         });
		
	}
}
