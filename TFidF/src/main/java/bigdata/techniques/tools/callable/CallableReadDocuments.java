package bigdata.techniques.tools.callable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bigdata.algorithms.Document;

public class CallableReadDocuments {

	private int count;
	private final int limit;
	private final List<String> urls;
	private ConcurrentHashMap<Document, Integer> documents;
	
	public CallableReadDocuments(int limit,
			List<String> urls,
			ConcurrentHashMap<Document, Integer> documents)
	{
		this.limit = limit;
		this.count = -1;
		this.urls = urls;
		this.documents = documents;
	}
	
	public boolean run () {
		Callable<Integer> callable = new CallableNextPosition(urls.size()-1);
		int numberOfThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		return true;
	}

}
