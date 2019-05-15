package bigdata.techniques.tools.forkJoin;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;

import bigdata.algorithms.Document;

public class ForkJoinTFidFTable extends RecursiveAction{

	private static final long serialVersionUID = 1L;
	private static final int SEQUENTIAL_THRESHOLD = 1;
	private final Set<Document> documents;
	private final List<String> terms;
	private final ConcurrentHashMap<String, Double> termFrequency;
	private final ConcurrentHashMap<String, Double> inverseDocument;
	private ConcurrentHashMap<String, Double> tfIdf;
	
	public ForkJoinTFidFTable(Set<Document> documents, List<String> terms,
			ConcurrentHashMap<String, Double> termFrequency, 
			ConcurrentHashMap<String, Double> inverseDocument,
			ConcurrentHashMap<String, Double> tfIdf)
	{
		this.documents = documents;
		this.terms = terms;
		this.termFrequency = termFrequency;
		this.inverseDocument = inverseDocument;
		this.tfIdf = tfIdf;
	}

	@Override
	protected void compute() {
		if (terms.size() <= SEQUENTIAL_THRESHOLD) {
			computeDirect(terms.get(0));
		}else {
			// calculate new range
			int mid = terms.size() / 2;
			ForkJoinTFidFTable firstSubtask = 
					new ForkJoinTFidFTable(documents, terms.subList(0, mid),
							termFrequency, inverseDocument, tfIdf);
			ForkJoinTFidFTable secondSubtask = 
					new ForkJoinTFidFTable(documents, terms.subList(mid, terms.size()),
							termFrequency, inverseDocument, tfIdf);
			
			firstSubtask.fork();     // queue the first task
			secondSubtask.compute(); // compute the second task
			firstSubtask.join();     // 	
		}
	}

	private void computeDirect (String s) {
		documents.stream()
				 .forEach( d -> {
					 StringBuffer keyValue = new StringBuffer("");
					 keyValue.append(s);
					 keyValue.append("+");
					 keyValue.append(d.getName());
						
					 if (this.termFrequency.get(keyValue.toString()) != null) {
						double tfValue = this.termFrequency.get(keyValue.toString());
						double value = tfValue * this.inverseDocument.get(s);
						this.tfIdf.put(keyValue.toString(), value);
				  	 }
				 });
	}
	
}
