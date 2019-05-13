package bigdata.techniques.tools.callable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class CallableReadDocuments implements Callable<Map<Document, Integer>> {

	private final MutexCounter counter;
	private final List<String> urls;
	
	public CallableReadDocuments(MutexCounter counter,
			List<String> urls) 
	{
		this.counter = counter;
		this.urls = urls;
	}
	
	@Override
	public Map<Document,Integer> call() throws Exception {
		Integer pos = null;
		Map<Document, Integer> doc = new HashMap<>();
		
		while ( (pos = counter.increment()) != null )
			doc.put(new Document(urls.get(pos)), pos);
		
		return doc;
	}
	
}
