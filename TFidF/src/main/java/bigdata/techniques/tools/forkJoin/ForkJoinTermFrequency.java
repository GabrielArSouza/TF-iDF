package bigdata.techniques.tools.forkJoin;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

import bigdata.algorithms.Document;

public class ForkJoinTermFrequency extends RecursiveAction{


	private static final long serialVersionUID = 1L;
	private static final int SEQUENTIAL_THRESHOLD = 1;
	private final List<Document> documents;
	private final ConcurrentHashMap<String, Integer> terms;
	private ConcurrentHashMap<String, Double> termFrequency;
	
	public ForkJoinTermFrequency (
			List<Document> documents,
			ConcurrentHashMap<String, Integer> terms,
			ConcurrentHashMap<String, Double> termFrequency)
	{
		this.documents = documents;
		this.terms = terms;
		this.termFrequency = termFrequency;
	}

	@Override
	protected void compute() {
		if (documents.size() <= SEQUENTIAL_THRESHOLD) {
			computeDirect(documents.get(0));
		}else {
			// calculate new range
			int mid = documents.size() / 2;
			ForkJoinTermFrequency firstSubtask = 
					new ForkJoinTermFrequency(documents.subList(0, mid),terms, termFrequency);
			ForkJoinTermFrequency secondSubtask = 
					new ForkJoinTermFrequency(documents.subList(mid, documents.size()), terms, termFrequency);
			
			firstSubtask.fork();     // queue the first task
			secondSubtask.compute(); // compute the second task
			firstSubtask.join();     // 	
		}
	}
	
	private void computeDirect(Document d) {
		Set<String> termsOfTable = terms.keySet();
		termsOfTable.stream()
		            .forEach( s -> {
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
	}
}
