package bigdata.techniques.tools.parallelStream;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class ParallelStreamReadDocuments {

	private List<String> data;
	private ConcurrentHashMap<Document, Integer> documents;
	
	public ParallelStreamReadDocuments
	( List<String> data, 
	  ConcurrentHashMap<Document, Integer> documents)
	{
		this.data = data;
		this.documents = documents;
	}
	
	public void run ()
	{
		data.parallelStream()
			.forEach( s -> documents.put(new Document(s), 1));
		
	}
}
