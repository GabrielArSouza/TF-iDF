package bigdata.techniques.tools.parallelStream;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class ParallelStreamTFidFTable {

	private final Set<Document> documents;
	private final Set<String> terms;
	private final ConcurrentHashMap<String, Double> termFrequency;
	private final ConcurrentHashMap<String, Double> inverseDocument;
	private ConcurrentHashMap<String, Double> tfIdf;
	
	public ParallelStreamTFidFTable 
		(Set<Document> documents,
		 Set<String> terms,
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
	
	public void run () {

		terms.parallelStream()
			 .forEach( s -> {
				 documents.parallelStream()
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
			 });
	}
	
}
