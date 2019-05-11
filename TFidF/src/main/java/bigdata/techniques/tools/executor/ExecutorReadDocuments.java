package bigdata.techniques.tools.executor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;
import bigdata.techniques.tools.mutex.MutexCounter;

public class ExecutorReadDocuments implements Runnable{

	private final MutexCounter mtxCounter;
	private final ArrayList<String> urls;
	private ConcurrentHashMap<Document, Integer> documents;
	
	public ExecutorReadDocuments(
			MutexCounter mtxCounter,
			ArrayList<String> urls,
			ConcurrentHashMap<Document, Integer> documents)
	{
		this.mtxCounter = mtxCounter;
		this.urls = urls;
		this.documents = documents;
		
	}
	
	@Override
	public void run() {
		Integer pos = null;
		while ((pos = mtxCounter.increment())!=null) {
			this.documents.put(new Document(urls.get(pos)), pos);
		}
	}
	
	
}
