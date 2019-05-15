package bigdata.techniques.tools.forkJoin;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

import bigdata.algorithms.Document;

public class ForkJoinInverseDocument extends RecursiveAction {

	private static final long serialVersionUID = 1L;
	private static final int SEQUENTIAL_THRESHOLD = 1;
	private final List<String> terms;
	private final Set<Document> documents;
	private ConcurrentHashMap<String, Double> inverseDistance;
	
	public ForkJoinInverseDocument(
			List<String> terms,
			Set<Document> documents,
			ConcurrentHashMap<String, Double> inverseDistance) 
	{
		this.terms = terms;
		this.documents = documents;
		this.inverseDistance = inverseDistance;
	}
	
	@Override
	protected void compute() {
		if (terms.size() <= SEQUENTIAL_THRESHOLD) {
			computeDirect(terms.get(0));
		}else {
			// calculate new range
			int mid = terms.size() / 2;
			ForkJoinInverseDocument firstSubtask = 
					new ForkJoinInverseDocument(terms.subList(0, mid),documents, inverseDistance);
			ForkJoinInverseDocument secondSubtask = 
					new ForkJoinInverseDocument(terms.subList(mid, terms.size()), documents, inverseDistance);
			
			firstSubtask.fork();     // queue the first task
			secondSubtask.compute(); // compute the second task
			firstSubtask.join();     // 	
		}
	}
	
	private void computeDirect (String term) {
		double numDocs = (double) this.documents.size();
		// count number of occurrences of term s
		 long count = 
		 documents.stream()
		  		  .filter( d -> d.numberOfOccurrencesTerm(term) != 0)
		 		  .count();
		 
		 // calculate inverse document
		 double value = Math.log(numDocs/(double)count);
		 this.inverseDistance.put(term, value);
	}

}
