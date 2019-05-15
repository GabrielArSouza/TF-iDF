package bigdata.techniques.tools.forkJoin;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

import bigdata.algorithms.Document;
import bigdata.common.StopWord;
import bigdata.common.StopWordHolder;

public class ForkJoinConstructTerms extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	private static final int SEQUENTIAL_THRESHOLD = 1;
	private final List<Document> documents;
	private ConcurrentHashMap<String, Integer> terms;
	
	public ForkJoinConstructTerms(
			List<Document> documents,
			ConcurrentHashMap<String, Integer> terms)
	{
		this.documents = documents;
		this.terms = terms;
	}
	
	@Override
	protected void compute() {
		if (documents.size() <= SEQUENTIAL_THRESHOLD) {
			computeDirect(documents.get(0));
		}else {
			
			// calculate new range
			int mid = documents.size() / 2;
			ForkJoinConstructTerms firstSubtask = 
					new ForkJoinConstructTerms(documents.subList(0, mid), terms);
			ForkJoinConstructTerms secondSubtask = 
					new ForkJoinConstructTerms(documents.subList(mid, documents.size()), terms);
			
			firstSubtask.fork();     // queue the first task
			secondSubtask.compute(); // compute the second task
			firstSubtask.join();     // 
		
		}		
	}
	
	private void computeDirect(Document d) {
		StopWord sw = StopWordHolder.getStopWord();
		Set<String> terms = d.getTableTermOccurrence().keySet();
		terms.stream()
		     .forEach( s -> {
		    	  if (!sw.isStopWord(s))
					 this.terms.put(s, 1);
		    	  else d.incrementStopWords(d.numberOfOccurrencesTerm(s));
			 });
	}

}
