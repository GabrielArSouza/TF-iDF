package bigdata.techniques.tools.mutex;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class MutexReadDocuments extends Thread {

	private final MutexCounter mtxCounter;
	private final ArrayList<String> urls;
	private ConcurrentHashMap<Document, Integer> documents;
	
	public MutexReadDocuments (MutexCounter mtxCounter, 
		   ArrayList<String> urls,
		   ConcurrentHashMap<Document, Integer> documents)
	{
		this.mtxCounter = mtxCounter;
		this.urls = urls;
		this.documents = documents;
	}
	
	public void run () {
		int pos = 0;
		while (!mtxCounter.isLimit()) {
			pos = mtxCounter.increment();
			this.documents.put(new Document(urls.get(pos)), pos);
		}
	}
	
}
