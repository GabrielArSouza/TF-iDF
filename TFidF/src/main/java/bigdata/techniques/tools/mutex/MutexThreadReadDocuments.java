package bigdata.techniques.tools.mutex;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class MutexThreadReadDocuments extends Thread {

	private final MutexCounter mtxCounter;
	private final ArrayList<String> urls;
	private ConcurrentHashMap<Document, Integer> documents;
	
	public MutexThreadReadDocuments (MutexCounter mtxCounter, 
		   ArrayList<String> urls,
		   ConcurrentHashMap<Document, Integer> documents)
	{
		this.mtxCounter = mtxCounter;
		this.urls = urls;
		this.documents = documents;
	}
	
	public void run () {
		Integer pos = null;
		while ((pos = mtxCounter.increment())!=null) {
			this.documents.put(new Document(urls.get(pos)), pos);
		}
	}
	
}
