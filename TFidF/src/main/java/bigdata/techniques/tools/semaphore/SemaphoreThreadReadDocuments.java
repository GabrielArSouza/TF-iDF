package bigdata.techniques.tools.semaphore;

import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class SemaphoreThreadReadDocuments extends Thread{

	private ConcurrentHashMap<Document, Integer> documents;
	private SemaphoreStringController semaphore;
	
	public SemaphoreThreadReadDocuments(
		   ConcurrentHashMap<Document, Integer> documents,
		   SemaphoreStringController semaphore) 
	{
		this.documents = documents;
		this.semaphore = semaphore;
	}
	
	public void run () {
		
		int count = 0;
		String actualUrl = null;
		try {
			while((actualUrl = semaphore.getNext()) != null) {
				this.documents.put(new Document(actualUrl.toString()), count);
				count++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
