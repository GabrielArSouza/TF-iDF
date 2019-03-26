package bigdata.techniques.tools.mutex;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import bigdata.algorithms.Document;

public class MutexThreadConstructTerms extends Thread {

	private final MutexCounter mtxCounter;
	private final ArrayList<Document> docs;
	private ConcurrentHashMap<String, Integer> terms;
	
	public MutexThreadConstructTerms 
	( MutexCounter mtxCounter, ArrayList<Document> docs,
      ConcurrentHashMap<String, Integer> terms)
	{
		this.mtxCounter = mtxCounter;
		this.docs = docs;
		this.terms = terms;
	}
	
	public void run () {
//		System.out.println("Terms: A thread " + this.getId() + " foi iniciada");
		int pos = 0;
		while (!mtxCounter.isLimit()) {
			pos = mtxCounter.increment();
			for (String s : this.docs.get(pos).getTableTermOccurrence().keySet())
				this.terms.put(s, 1);
		}
//		System.out.println("A thread " + this.getId() + " terminou");
	}
	
}
