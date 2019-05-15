package bigdata.techniques;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import bigdata.algorithms.Document;
import bigdata.algorithms.TFidF;
import bigdata.techniques.tools.callable.CallableConstructTerms;
import bigdata.techniques.tools.callable.CallableInverseDistance;
import bigdata.techniques.tools.callable.CallableReadDocuments;
import bigdata.techniques.tools.callable.CallableTFidFTable;
import bigdata.techniques.tools.callable.CallableTermFrequency;
import bigdata.techniques.tools.mutex.MutexCounter;

public class CallableTFidF extends TFidF {

	public CallableTFidF(String url) {
		super(url);
	}

	@Override
	public void readDocuments() {
		ArrayList<String> urls = this.readURLs();
		MutexCounter mtx = new MutexCounter(urls.size()-1);
		ExecutorService exec = Executors.newFixedThreadPool(10);
		
		List<Future<Map<Document, Integer>>> resultList = new ArrayList<>();
		
		for (int i = 0; i < 10; i++) {
			CallableReadDocuments c = new CallableReadDocuments(mtx, urls);
			Future<Map<Document,Integer>> result = exec.submit(c);
			resultList.add(result);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		resultList.stream()
				  .forEach(f -> {
					  try { this.documents.putAll( f.get());}
					  catch (InterruptedException | ExecutionException e) 
					  {	 e.printStackTrace(); }
				  });

	}

	@Override
	public void constructTerms() {
		
		ArrayList<Document> docs = new ArrayList<Document>();
		docs.addAll(documents.keySet());
		
		// Get Counter
		MutexCounter count = new MutexCounter(docs.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);

		List<Future<Map<String, Integer>>> resultList = new ArrayList<>();
		
		for (int i = 0; i < numberOfThreads; i++) {
			CallableConstructTerms c = new CallableConstructTerms(count, docs);
			Future<Map<String,Integer>> result = exec.submit(c);
			resultList.add(result);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		resultList.stream()
		  .forEach(f -> {
			  try { this.terms.putAll( f.get());}
			  catch (InterruptedException | ExecutionException e) 
			  {	 e.printStackTrace(); }
		  });
	}

	@Override
	public void termFrequency() {
		
		// Get document list
		ArrayList<Document> docs = new ArrayList<Document>();
		docs.addAll(documents.keySet());
				
		// Get Counter
		MutexCounter count = new MutexCounter(docs.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		List<Future<Map<String, Double>>> resultList = new ArrayList<>();

		for (int i = 0; i < numberOfThreads; i++) {
			CallableTermFrequency c = new CallableTermFrequency(count, docs, terms);
			Future<Map<String,Double>> result = exec.submit(c);
			resultList.add(result);
		}
		
		exec.shutdown();
		
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		resultList.stream()
		  .forEach(f -> {
			  try { this.termFrequency.putAll( f.get());}
			  catch (InterruptedException | ExecutionException e) 
			  {	 e.printStackTrace(); }
		  });
	}

	@Override
	public void inverseDistance() {
		
		ArrayList<String> terms = new ArrayList<String>();
		terms.addAll(this.terms.keySet());

		MutexCounter mtxCounter = new MutexCounter(this.terms.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		List<Future<Map<String, Double>>> resultList = new ArrayList<>();

		for (int i = 0; i < numberOfThreads; i++) {
			CallableInverseDistance c = new CallableInverseDistance(mtxCounter, terms, documents);
			Future<Map<String,Double>> result = exec.submit(c);
			resultList.add(result);
		}
		
		exec.shutdown();
		
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		resultList.stream()
		  .forEach(f -> {
			  try { this.inverseDistance.putAll( f.get());}
			  catch (InterruptedException | ExecutionException e) 
			  {	 e.printStackTrace(); }
		  });
	}

	@Override
	public void tfidfTable() {
		
		MutexCounter count = new MutexCounter(terms.size()-1);
		int numberOfThreads = this.getNumberOfCores();
		ExecutorService exec = Executors.newFixedThreadPool(numberOfThreads);
		
		List<Future<Map<String, Double>>> resultList = new ArrayList<>();
		
		for (int i = 0; i < numberOfThreads; i++) {
			CallableTFidFTable c = new CallableTFidFTable(count, documents, 
					terms, termFrequency, inverseDistance);
			Future<Map<String,Double>> result = exec.submit(c);
			resultList.add(result);
		}
		
		exec.shutdown();
		
		try {
			exec.awaitTermination(1, TimeUnit.HOURS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		resultList.stream()
		  .forEach(f -> {
			  try { this.tfIdf.putAll( f.get());}
			  catch (InterruptedException | ExecutionException e) 
			  {	 e.printStackTrace(); }
		  });
	}
	
}
